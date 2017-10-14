package de.ubuntix.android.birthdayreminder;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class Debug {

	public static void logDatabase(Context context) {
		// birthdays
		{
			String[] projection = new String[] { Data._ID, Data.RAW_CONTACT_ID, Data.DISPLAY_NAME, Data.DATA1 };
			String selection = String.format("%s == '%s' AND %s == %d", Data.MIMETYPE, Event.CONTENT_ITEM_TYPE,
					Data.DATA2, Event.TYPE_BIRTHDAY);
			String sortOrder = Data.DATA1;

			Cursor cursor = context.getContentResolver()
					.query(Data.CONTENT_URI, projection, selection, null, sortOrder);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String line = "";
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (!cursor.isNull(i)) {
						line += cursor.getString(i) + ", ";
					} else {
						line += "null, ";
					}
				}
				cursor.moveToNext();
				Log.w("tag", line);
			}
			cursor.close();
		}
		// raw contacts
		{
			String[] projection = new String[] { RawContacts._ID, RawContacts.CONTACT_ID, RawContacts.ACCOUNT_NAME,
					RawContacts.ACCOUNT_TYPE };
			String selection = null; // String.format("%s == '%s'", RawContacts.ACCOUNT_NAME, this.account_name);
			String sortOrder = null;

			Cursor cursor = context.getContentResolver().query(RawContacts.CONTENT_URI, projection, selection, null,
					sortOrder);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String line = "";
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (!cursor.isNull(i)) {
						line += cursor.getString(i) + ", ";
					} else {
						line += "null, ";
					}
				}
				cursor.moveToNext();
				Log.w("tag", line);
			}
			cursor.close();
		}
		// contacts
		{
			String[] projection = new String[] { Contacts._ID, Contacts.DISPLAY_NAME };
			String selection = null;
			String sortOrder = null;

			Cursor cursor = context.getContentResolver().query(Contacts.CONTENT_URI, projection, selection, null,
					sortOrder);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String line = "";
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (!cursor.isNull(i)) {
						line += cursor.getString(i) + ", ";
					} else {
						line += "null, ";
					}
				}
				cursor.moveToNext();
				Log.w("tag", line);
			}
			cursor.close();
		}
	}

}
