package de.ubuntix.android.birthdayreminder.view;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import de.ubuntix.android.birthdayreminder.R;
import de.ubuntix.android.birthdayreminder.database.Database;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.model.RawContact;
import de.ubuntix.android.birthdayreminder.util.CalendarUtils;

public class DateOfBirthEditDialog implements OnClickListener, OnCancelListener, OnDateChangedListener {

	private Context context;
	private Runnable dateChangedAction;
	private AlertDialog dialog;
	private RawContact rawContact;
	private DateOfBirth dateOfBirth;
	private Calendar date;

	public void show(Context context, RawContact rawContact, DateOfBirth dateOfBirth, Runnable dateChangedAction) {
		// get date
		this.context = context;
		this.dateChangedAction = dateChangedAction;
		this.rawContact = rawContact;
		this.dateOfBirth = dateOfBirth;

		this.date = CalendarUtils.todaysCalendar();
		this.date.add(Calendar.YEAR, -25);
		if (this.dateOfBirth != null) {
			this.date.setTime(this.dateOfBirth.getDate().getTime());
		}

		// create dialog and set title and text
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setTitle(rawContact.getContact().getName());

		DatePicker timePicker = new DatePicker(context);
		dialogBuilder.setView(timePicker);
		timePicker.init(this.date.get(Calendar.YEAR), this.date.get(Calendar.MONTH),
				this.date.get(Calendar.DAY_OF_MONTH), this);

		dialogBuilder.setPositiveButton(R.string.ok, this);
		dialogBuilder.setNegativeButton(R.string.cancel, this);
		dialogBuilder.setOnCancelListener(this);

		this.dialog = dialogBuilder.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE
				&& (this.dateOfBirth == null || !this.dateOfBirth.getDate().equals(this.date))) {
			Database db = new Database(this.context.getContentResolver());

			if (this.dateOfBirth == null) {
				db.createDateOfBirth(this.rawContact, this.date);
			} else {
				db.updateDateOfBirth(this.dateOfBirth, this.date);
			}

			this.dateChangedAction.run();
		}

		dialog.dismiss();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.dialog.dismiss();
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		this.date.set(year, monthOfYear, dayOfMonth);
	}
}
