package de.ubuntix.android.birthdayreminder.model;

import java.util.Calendar;

public interface DateOfBirth {

	public int getId();

	public RawContact getRawContact();

	public Calendar getDate();
}
