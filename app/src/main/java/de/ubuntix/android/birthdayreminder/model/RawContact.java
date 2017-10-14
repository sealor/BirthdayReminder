package de.ubuntix.android.birthdayreminder.model;

import java.util.List;

public interface RawContact {

	public int getId();

	public Contact getContact();

	public String getAccountType();

	public String getAccountName();

	public List<DateOfBirth> getDatesOfBirth();
}