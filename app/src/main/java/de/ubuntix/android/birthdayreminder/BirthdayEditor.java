package de.ubuntix.android.birthdayreminder;

import java.util.LinkedHashMap;
import java.util.Map;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import de.ubuntix.android.birthdayreminder.database.Database;
import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.model.RawContact;
import de.ubuntix.android.birthdayreminder.view.DateOfBirthEditDialog;
import de.ubuntix.android.birthdayreminder.view.adapter.AccountDatesOfBirthAdapter;
import de.ubuntix.android.birthdayreminder.view.adapter.MultiListAdapter;

public class BirthdayEditor extends Activity implements OnItemClickListener {
	private static final int CREATE_BIRTHDAY = 0;
	private static final int EDIT_BIRTHDAY = 1;
	private static final int DELETE_BIRTHDAY = 2;

	public static final String CONTACT_ID = "contact_id";
	private static final int NO_CONTACT_ID = Integer.MIN_VALUE;

	private TextView name;
	private ListView list;

	private Database db;

	private Contact contact;

	private Map<String, AuthenticatorDescription> map = new LinkedHashMap<String, AuthenticatorDescription>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);

		// bind GUI components
		this.name = (TextView) findViewById(R.id.editor_name);
		this.list = (ListView) findViewById(R.id.editor_list);

		// check if contact id is valid
		this.db = new Database(getContentResolver());
		int contactId = getIntent().getIntExtra(CONTACT_ID, NO_CONTACT_ID);
		this.contact = this.db.getContact(contactId);
		if (this.contact == null) {
			finish();
		}
		this.name.setText(this.contact.getName());

		// pre-load information about all account types
		AuthenticatorDescription[] authTypes = AccountManager.get(this).getAuthenticatorTypes();
		for (AuthenticatorDescription authDesc : authTypes) {
			this.map.put(authDesc.type, authDesc);
		}

		// bind list events
		this.list.setOnItemClickListener(this);
		this.list.setOnCreateContextMenuListener(this);

		// create the GUI
		updateView();
	}

	private void updateView() {
		MultiListAdapter adapter = new MultiListAdapter();

		for (RawContact rawContact : this.contact.getRawContacts()) {
			String accountType = getResources().getString(R.string.editor_phone);
			String accountName = rawContact.getAccountName();

			Drawable accountIcon = getResources().getDrawable(R.drawable.stat_sys_phone_call);

			try {
				AuthenticatorDescription authDesc = this.map.get(rawContact.getAccountType());
				Resources accountRes = getPackageManager().getResourcesForApplication(authDesc.packageName);

				accountIcon = accountRes.getDrawable(authDesc.iconId);
				accountType = accountRes.getString(authDesc.labelId);
			} catch (Exception e) {
			}

			AccountDatesOfBirthAdapter accountAdapter = new AccountDatesOfBirthAdapter(this, new NewDateOfBirthAction(
					rawContact), accountIcon, accountType, accountName);

			accountAdapter.getDatesOfBirth().addAll(rawContact.getDatesOfBirth());
			adapter.getListAdapters().add(accountAdapter);
		}

		this.list.setAdapter(adapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) menuInfo;
		DateOfBirth dateOfBirth = (DateOfBirth) this.list.getAdapter().getItem(adapterMenuInfo.position);

		menu.setHeaderTitle(R.string.contextMenuTitle);
		if (dateOfBirth == null) {
			menu.add(Menu.NONE, CREATE_BIRTHDAY, Menu.NONE, R.string.contextMenu_create);
		} else {
			menu.add(Menu.NONE, EDIT_BIRTHDAY, Menu.NONE, R.string.contextMenu_edit);
			menu.add(Menu.NONE, DELETE_BIRTHDAY, Menu.NONE, R.string.contextMenu_delete);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adapterMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		DateOfBirth dateOfBirth = (DateOfBirth) this.list.getAdapter().getItem(adapterMenuInfo.position);

		if (item.getItemId() == DELETE_BIRTHDAY) {
			// DELETE
			if (dateOfBirth != null) {
				this.db.deleteDateOfBirth(dateOfBirth);
			}

			updateView();
		} else {
			// EDIT
			RawContact rawContact = dateOfBirth.getRawContact();
			new DateOfBirthEditDialog().show(this, rawContact, dateOfBirth, new Runnable() {
				@Override
				public void run() {
					updateView();
				}
			});
		}

		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DateOfBirth dateOfBirth = (DateOfBirth) parent.getAdapter().getItem(position);
		RawContact rawContact = dateOfBirth.getRawContact();

		// EDIT
		new DateOfBirthEditDialog().show(this, rawContact, dateOfBirth, new Runnable() {
			@Override
			public void run() {
				updateView();
			}
		});
	}

	private class NewDateOfBirthAction implements Runnable {
		private final RawContact rawContact;

		public NewDateOfBirthAction(RawContact rawContact) {
			this.rawContact = rawContact;
		}

		@Override
		public void run() {
			// CREATE
			new DateOfBirthEditDialog().show(BirthdayEditor.this, this.rawContact, null, new Runnable() {
				@Override
				public void run() {
					updateView();
				}
			});
		}
	}
}
