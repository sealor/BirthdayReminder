package de.ubuntix.android.birthdayreminder.database.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.util.CalendarUtils;
import de.ubuntix.android.birthdayreminder.util.DatabaseUtils;

public class ContactDataAccess {

	private ContentResolver contentResolver;

	public ContactDataAccess(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public List<Contact> getAll() {
		List<DBContact> dbContacts = getDBContactsById();
		List<DBRawContact> dbRawContacts = getDBRawContactsById();
		List<DBData> dbDatas = getDBDatasByRawContactId();

		Map<Integer, ContactImpl> contacts = new LinkedHashMap<Integer, ContactImpl>();
		for (DBContact dbContact : dbContacts) {
			ContactImpl contact = new ContactImpl(dbContact._id, dbContact.display_name);
			contacts.put(dbContact._id, contact);
		}

		Map<Integer, RawContactImpl> rawContacts = new LinkedHashMap<Integer, RawContactImpl>();
		for (DBRawContact dbRawContact : dbRawContacts) {
			ContactImpl contact = contacts.get(dbRawContact.contact_id);
			if (contact != null) {
				RawContactImpl rawContact = new RawContactImpl(dbRawContact._id, contact, dbRawContact.account_type,
						dbRawContact.account_name);
				contact.rawContacts.add(rawContact);
				rawContacts.put(dbRawContact._id, rawContact);
			}
		}

		for (DBData dbData : dbDatas) {
			RawContactImpl rawContact = rawContacts.get(dbData.raw_contact_id);
			if (rawContact != null) {
				Calendar date;
				date = CalendarUtils.parseCalendar(dbData.data1);
				if (date != null) {
					DateOfBirth dateOfBirth = new DateOfBirthImpl(dbData._id, rawContact, date);
					rawContact.datesOfBirth.add(dateOfBirth);
				}
			}
		}

		return new ArrayList<Contact>(contacts.values());
	}

	public Contact get(int contactId) {
		for (Contact contact : getAll()) {
			if (contact.getId() == contactId) {
				return contact;
			}
		}
		return null;
	}

	private List<DBContact> getDBContactsById() {
		String[] projection = new String[] { Contacts._ID, Contacts.DISPLAY_NAME };
		String selection = null;
		String sortOrder = null;

		Cursor cursor = this.contentResolver.query(Contacts.CONTENT_URI, projection, selection, null, sortOrder);

		return DatabaseUtils.createList(DBContact.class, cursor);
	}

	private List<DBRawContact> getDBRawContactsById() {
		String[] projection = new String[] { RawContacts._ID, RawContacts.CONTACT_ID, RawContacts.ACCOUNT_TYPE,
				RawContacts.ACCOUNT_NAME };
		String selection = null;
		String sortOrder = null;

		Cursor cursor = this.contentResolver.query(RawContacts.CONTENT_URI, projection, selection, null, sortOrder);

		return DatabaseUtils.createList(DBRawContact.class, cursor);
	}

	private List<DBData> getDBDatasByRawContactId() {
		String[] projection = new String[] { Data._ID, Data.RAW_CONTACT_ID, Data.DATA1 };
		String selection = String.format("%s == '%s' AND %s == %d", Data.MIMETYPE, Event.CONTENT_ITEM_TYPE, Data.DATA2,
				Event.TYPE_BIRTHDAY);
		String sortOrder = Data.DATA1;

		Cursor cursor = this.contentResolver.query(Data.CONTENT_URI, projection, selection, null, sortOrder);

		return DatabaseUtils.createList(DBData.class, cursor);
	}

	public static class DBContact {
		private int _id;
		private String display_name;
	}

	public static class DBRawContact {
		private int _id;
		private int contact_id;
		private String account_type;
		private String account_name;
	}

	public static class DBData {
		private int _id;
		private int raw_contact_id;
		private String data1;
	}
}
