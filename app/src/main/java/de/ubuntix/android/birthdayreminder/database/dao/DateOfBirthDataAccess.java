package de.ubuntix.android.birthdayreminder.database.dao;

import java.util.Calendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Data;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.model.RawContact;
import de.ubuntix.android.birthdayreminder.util.CalendarUtils;

public class DateOfBirthDataAccess {

	private ContentResolver contentResolver;

	public DateOfBirthDataAccess(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public void create(RawContact rawContact, Calendar date) {
		RawContactImpl rawContactImpl = (RawContactImpl) rawContact;

		// TODO: check for existing Contact and rawContact
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactImpl.getId());
		values.put(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE);
		values.put(Data.DATA2, Event.TYPE_BIRTHDAY);
		values.put(Data.DATA1, CalendarUtils.formatCalendar(date));

		Uri uri = this.contentResolver.insert(Data.CONTENT_URI, values);

		// sync data structure
		if (uri != null) {
			int id = Integer.parseInt(uri.getLastPathSegment());
			DateOfBirth dateOfBirthData = new DateOfBirthImpl(id, rawContactImpl, date);
			rawContactImpl.datesOfBirth.add(dateOfBirthData);
		}
	}

	public void update(DateOfBirth dateOfBirth, Calendar newDate) {
		// TODO: check for existing Contact and rawContact
		String where = Data._ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(((DateOfBirthImpl) dateOfBirth).id) };

		ContentValues values = new ContentValues();
		values.put(Data.DATA1, CalendarUtils.formatCalendar(newDate));

		int linesUpdated = this.contentResolver.update(Data.CONTENT_URI, values, where, selectionArgs);

		// sync data structure
		if (linesUpdated == 1) {
			dateOfBirth.getDate().setTime(newDate.getTime());
		}
	}

	public void delete(DateOfBirth dateOfBirth) {
		// TODO: check for existing Contact and rawContact
		String where = Data._ID + "=?";
		String[] selectionArgs = new String[] { String.valueOf(((DateOfBirthImpl) dateOfBirth).id) };

		int linesDeleted = this.contentResolver.delete(Data.CONTENT_URI, where, selectionArgs);

		// sync data structure
		if (linesDeleted == 1) {
			RawContactImpl rawContactImpl = (RawContactImpl) dateOfBirth.getRawContact();
			rawContactImpl.datesOfBirth.remove(dateOfBirth);
		}
	}
}
