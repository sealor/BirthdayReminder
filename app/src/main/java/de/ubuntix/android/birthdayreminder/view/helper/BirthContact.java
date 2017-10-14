package de.ubuntix.android.birthdayreminder.view.helper;

import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;

public class BirthContact {

	private final Contact contact;
	private final DateOfBirth dateOfBirth;

	public BirthContact(Contact contact, DateOfBirth dateOfBirth) {
		super();

		this.contact = contact;
		this.dateOfBirth = dateOfBirth;
	}

	public Contact getContact() {
		return contact;
	}

	public DateOfBirth getDateOfBirth() {
		return dateOfBirth;
	}

	@Override
	public String toString() {
		if (this.dateOfBirth != null) {
			return String.format("%s (%s)", this.contact.getName(), this.getDateOfBirth().getDate());
		}
		return this.contact.getName();
	}
}
