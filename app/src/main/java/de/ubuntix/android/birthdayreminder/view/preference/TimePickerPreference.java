package de.ubuntix.android.birthdayreminder.view.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerPreference extends DialogPreference implements TimePicker.OnTimeChangedListener {

	private String defaultTime = "09:00";
	private String time = defaultTime;
	private CharSequence unformatedSummary = null;
	private TimePicker timePicker;

	public TimePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPersistent(true);
		this.unformatedSummary = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
	}

	public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPersistent(true);
		this.unformatedSummary = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "summary");
	}

	@Override
	protected void onBindView(View view) {
		setAndFormatSummary(this.unformatedSummary);
		super.onBindView(view);
	}

	@Override
	protected View onCreateDialogView() {

		this.timePicker = new TimePicker(getContext());
		this.timePicker.setIs24HourView(true);
		this.timePicker.setOnTimeChangedListener(this);

		int h = getHour();
		int m = getMinute();
		if (h >= 0 && h <= 24 && m >= 0 && m <= 60) {
			this.timePicker.setCurrentHour(h);
			this.timePicker.setCurrentMinute(m);
		}

		return this.timePicker;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			this.timePicker.clearFocus();
			persistString(this.time);
		} else {
			this.time = getPersistedString(defaultTime);
		}
		setAndFormatSummary(this.unformatedSummary);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hour, int minute) {
		setTime(hour, minute);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (defaultValue instanceof String) {
			this.defaultTime = (String) defaultValue;
		}

		if (restorePersistedValue) {
			this.time = getPersistedString(this.defaultTime);
		} else {
			this.time = this.defaultTime;
		}
	}

	private void setTime(int hour, int minute) {
		this.time = String.format("%02d:%02d", hour, minute);
	}

	private int getHour() {
		return Integer.parseInt(this.time.split(":")[0]);
	}

	private int getMinute() {
		return Integer.parseInt(this.time.split(":")[1]);
	}

	@Override
	public void setSummary(CharSequence summary) {
		this.unformatedSummary = summary;
	}

	private void setAndFormatSummary(CharSequence summary) {
		if (summary != null) {
			summary = summary.toString().replaceAll("@value\\b", this.time);
		}
		super.setSummary(summary);
	}
}