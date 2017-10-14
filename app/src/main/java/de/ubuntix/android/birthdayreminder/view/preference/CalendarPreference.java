package de.ubuntix.android.birthdayreminder.view.preference;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class CalendarPreference extends DialogPreference implements OnDateChangedListener {

	private Calendar editCalendar;
	private DatePicker timePicker;

	public CalendarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.editCalendar = Calendar.getInstance();
		this.editCalendar.clear();
	}

	public void setCalendar(Calendar calendar) {
		this.editCalendar = (Calendar) calendar.clone();
	}

	public void setCalendar(Date date) {
		this.editCalendar = Calendar.getInstance();
		this.editCalendar.clear();
		this.editCalendar.setTime(date);
	}

	public void setCalendar(int year, int month, int day) {
		this.editCalendar = Calendar.getInstance();
		this.editCalendar.clear();
		this.editCalendar.set(year, month, day);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// work around: remove arrow of the preference
		ViewGroup viewGroup = (ViewGroup) super.onCreateView(parent);
		if (viewGroup.getChildCount() == 2) {
			viewGroup.removeViewAt(1);
		}
		return viewGroup;
	}

	@Override
	protected View onCreateDialogView() {
		this.timePicker = new DatePicker(getContext());
		timePicker.init(editCalendar.get(Calendar.YEAR), editCalendar.get(Calendar.MONTH),
				editCalendar.get(Calendar.DAY_OF_MONTH), this);
		return timePicker;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			this.timePicker.clearFocus();
			callChangeListener(this.editCalendar);
		}
		super.onDialogClosed(positiveResult);
	}

	@Override
	public void onDateChanged(DatePicker arg0, int year, int month, int day) {
		this.editCalendar.set(year, month, day);
	}

	public void showDialog() {
		showDialog(null);
	}
}
