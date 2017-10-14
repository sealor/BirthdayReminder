package de.ubuntix.android.birthdayreminder.database.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.model.RawContact;

public class RawContactImpl implements RawContact {

	private final int id;
	private final Contact contact;
	private final String accountName;
	private final String accountType;
	protected List<DateOfBirth> datesOfBirth = new ArrayList<DateOfBirth>(2);

	protected RawContactImpl(int id, Contact contact, String accountType, String accountName) {
		super();

		this.id = id;
		this.contact = contact;
		this.accountType = accountType;
		this.accountName = accountName;
	}

	public int getId() {
		return id;
	}

	@Override
	public Contact getContact() {
		return contact;
	}

	public String getAccountType() {
		return accountType;
	}

	@Override
	public String getAccountName() {
		return accountName;
	}

	@Override
	public List<DateOfBirth> getDatesOfBirth() {
		return Collections.unmodifiableList(this.datesOfBirth);
	}
}
