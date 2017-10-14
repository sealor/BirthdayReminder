package de.ubuntix.android.birthdayreminder.view.adapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.ubuntix.android.birthdayreminder.R;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;

public class AccountDatesOfBirthAdapter extends BaseAdapter implements OnClickListener {

	private final DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL);

	private final LayoutInflater inflater;
	private final Drawable accountIcon;
	private final String accountType;
	private final String accountName;

	private final Runnable newAction;

	private final List<DateOfBirth> datesOfBirth = new ArrayList<DateOfBirth>();

	public AccountDatesOfBirthAdapter(Context context, Runnable newAction, Drawable accountIcon, String accountType,
			String accountName) {
		this.inflater = LayoutInflater.from(context);
		this.accountIcon = accountIcon;
		this.accountType = accountType;
		this.accountName = accountName;
		this.newAction = newAction;
	}

	public List<DateOfBirth> getDatesOfBirth() {
		return datesOfBirth;
	}

	@Override
	public int getCount() {
		return this.datesOfBirth.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return this.accountType;
		} else {
			return this.datesOfBirth.get(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			if (convertView == null) {
				convertView = this.inflater.inflate(R.layout.editor_account, null);
			}

			ImageView accountIcon = (ImageView) convertView.findViewById(R.id.editor_icon);
			accountIcon.setImageDrawable(this.accountIcon);

			TextView accountType = (TextView) convertView.findViewById(R.id.editor_type);
			accountType.setText(this.accountType);

			TextView accountName = (TextView) convertView.findViewById(R.id.editor_name);
			accountName.setText(this.accountName);

			Button addDateOfBirth = (Button) convertView.findViewById(R.id.editor_new);
			addDateOfBirth.setOnClickListener(this);
		} else {
			DateOfBirth dateOfBirth = this.datesOfBirth.get(position - 1);

			if (convertView == null) {
				convertView = this.inflater.inflate(R.layout.editor_dateofbirth, null);
			}

			TextView date = (TextView) convertView.findViewById(R.id.editor_date);
			date.setText(dateFormatter.format(dateOfBirth.getDate().getTime()));
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public void onClick(View v) {
		this.newAction.run();
	}
}
