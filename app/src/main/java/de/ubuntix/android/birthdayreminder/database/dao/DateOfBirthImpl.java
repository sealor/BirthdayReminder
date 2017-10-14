package de.ubuntix.android.birthdayreminder.database.dao;

import java.util.Calendar;

import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.model.RawContact;

public class DateOfBirthImpl implements DateOfBirth {

	protected final int id;
	private final RawContact rawContact;
	private final Calendar date;

	protected DateOfBirthImpl(int id, RawContact rawContact, Calendar date) {
		super();

		this.id = id;
		this.rawContact = rawContact;
		this.date = date;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public RawContact getRawContact() {
		return rawContact;
	}

	@Override
	public Calendar getDate() {
		return date;
	}
}
