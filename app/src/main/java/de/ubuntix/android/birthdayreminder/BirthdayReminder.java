package de.ubuntix.android.birthdayreminder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.ubuntix.android.birthdayreminder.database.Database;
import de.ubuntix.android.birthdayreminder.database.Preferences;
import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.service.BirthdayBroadcastReceiver;
import de.ubuntix.android.birthdayreminder.view.adapter.BirthContactAdapter;
import de.ubuntix.android.birthdayreminder.view.adapter.CategoryAdapter;
import de.ubuntix.android.birthdayreminder.view.adapter.MultiListAdapter;
import de.ubuntix.android.birthdayreminder.view.comparator.BirthContactBirthdayComparator;
import de.ubuntix.android.birthdayreminder.view.comparator.BirthContactNameComparator;
import de.ubuntix.android.birthdayreminder.view.helper.BirthContact;
import de.ubuntix.android.birthdayreminder.view.helper.BirthContactHelper;

public class BirthdayReminder extends ListActivity {
	// TODO: call/write message on birthday
	// TODO: hideNotificationPref

	private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
	private final DateFormatSymbols dateSymbols = new DateFormatSymbols();

	public static final String CHANNEL_ID = BirthdayReminder.class.getName();

	private Database db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createNotificationChannel();



		this.db = new Database(getContentResolver());

		// debug code
		// Debug.logDatabase(this);

		// start BirthdayBroadcastReceiver if it is activated
		Preferences prefs = Preferences.getInstance(getApplicationContext());
		if (prefs.getActivateService()) {
			BirthdayBroadcastReceiver.restart(getApplicationContext());
		}

		// request runtime permission
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!isContactsPermissionGranted()) {
				requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
			}
		}
	}

	private void createNotificationChannel() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = getString(R.string.notificationChannelName);
			String description = getString(R.string.notificationDescription);
			int importance = NotificationManager.IMPORTANCE_HIGH;

			NotificationChannel notificationChannel = new NotificationChannel(
					CHANNEL_ID,
					name,
					importance);
			notificationChannel.setDescription(description);

			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			assert notificationManager != null;
			notificationManager.createNotificationChannel(notificationChannel);
		}
	}

	private boolean isContactsPermissionGranted() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isContactsPermissionGranted()) {
			updateView();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
			if (isContactsPermissionGranted()) {
				updateView();
			} else {
				Toast.makeText(this, R.string.no_contacts_permission, Toast.LENGTH_LONG).show();
			}
		}
	}

	private void updateView() {
		// create new list adapter

		MultiListAdapter listAdapter = new MultiListAdapter();
		List<ListAdapter> adapterList = listAdapter.getListAdapters();



		// load birthday and contact information
		List<Contact> contacts = this.db.getAllContacts();
		List<BirthContact> birthContacts = BirthContactHelper.createBirthContactList(contacts);

		// group all contacts by known and unknown birthday
		SortedSet<BirthContact> knownBirthdays = new TreeSet<>(new BirthContactBirthdayComparator());
		SortedSet<BirthContact> unknownBirthdays = new TreeSet<>(new BirthContactNameComparator());

		for (BirthContact birthContact : birthContacts) {
			DateOfBirth dateOfBirth = birthContact.getDateOfBirth();

			if (dateOfBirth != null) {
				knownBirthdays.add(birthContact);
			} else {
				unknownBirthdays.add(birthContact);
			}
		}

		Integer currentMonth = null;
		BirthContactAdapter currentBirthContactAdapter = null;
		String[] monthStrs = this.dateSymbols.getMonths();
		for (BirthContact birthContact : knownBirthdays) {
			int month = birthContact.getDateOfBirth().getDate().get(Calendar.MONTH);

			if (currentMonth == null || currentMonth != month) {
				currentMonth = month;

				currentBirthContactAdapter = new BirthContactAdapter(this);
				adapterList.add(new CategoryAdapter(this, monthStrs[currentMonth]));


				adapterList.add(currentBirthContactAdapter);
			}

			currentBirthContactAdapter.add(birthContact);


		}

		adapterList.add(new CategoryAdapter(this, getResources().getString(R.string.unknownBirthdays)));

		adapterList.add(new BirthContactAdapter(this, unknownBirthdays));


		setListAdapter(listAdapter);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		BirthContact birthContact = (BirthContact) l.getAdapter().getItem(position);
		Intent editorIntent = new Intent(this, BirthdayEditor.class);
		editorIntent.putExtra(BirthdayEditor.CONTACT_ID, birthContact.getContact().getId());
		startActivity(editorIntent);
	}

	@Override
	@SuppressLint("ResourceType")
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		int itemId = item.getItemId();

		if (itemId == R.id.preferences) {
			startActivity(new Intent(this, PreferenceWindow.class));
			return true;
		}
		if (itemId == R.id.quit) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
