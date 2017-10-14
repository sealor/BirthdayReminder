package de.ubuntix.android.birthdayreminder.view.adapter;

import java.util.Calendar;
import java.util.Collection;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.ubuntix.android.birthdayreminder.R;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.util.CalendarUtils;
import de.ubuntix.android.birthdayreminder.view.helper.BirthContact;

public class BirthContactAdapter extends ArrayAdapter<BirthContact> {

	private final Calendar today;
	private final LayoutInflater inflater;
	private final Resources res;

	public BirthContactAdapter(Context context) {
		super(context, R.layout.birth_contact);

		this.today = CalendarUtils.todaysCalendar();
		this.inflater = LayoutInflater.from(context);
		this.res = context.getResources();
	}

	public BirthContactAdapter(Context context, Collection<BirthContact> birthContacts) {
		this(context);

		for (BirthContact birthContact : birthContacts) {
			add(birthContact);
		}
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// create view if needed
		if (view == null) {
			view = this.inflater.inflate(R.layout.birth_contact, null);
		}

		// set name
		BirthContact birthContact = getItem(position);
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(birthContact.getContact().getName());

		// set dateOfBirth if available
		if (birthContact.getDateOfBirth() != null) {
			DateOfBirth dateOfBirth = birthContact.getDateOfBirth();

			Calendar date = dateOfBirth.getDate();
			Calendar nextBirthday = CalendarUtils.nextBirthday(today, date);
			int days = CalendarUtils.timeSpanInDays(today, nextBirthday);
			int newAge = nextBirthday.get(Calendar.YEAR) - date.get(Calendar.YEAR);

			TextView info = (TextView) view.findViewById(R.id.info);
			info.setText(getBirthdayText(days, newAge));
		}

		return view;
	}

	private String getBirthdayText(int days, int newAge) {
		String text;
		switch (days) {
		case 0:
			text = String.format(res.getString(R.string.summary_today), newAge);
			break;
		case 1:
			text = String.format(res.getString(R.string.summary_tomorrow), newAge);
			break;
		default:
			text = String.format(res.getString(R.string.summary_other), days, newAge);
		}
		return text;
	}
}
