package de.ubuntix.android.birthdayreminder.database.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.RawContact;

public class ContactImpl implements Contact {

	private final int id;
	private final String name;
	protected List<RawContact> rawContacts = new ArrayList<RawContact>(4);

	protected ContactImpl(int id, String name) {
		super();

		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<RawContact> getRawContacts() {
		return Collections.unmodifiableList(rawContacts);
	}
}
