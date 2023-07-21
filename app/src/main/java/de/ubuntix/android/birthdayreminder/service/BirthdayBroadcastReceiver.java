package de.ubuntix.android.birthdayreminder.service;

import static de.ubuntix.android.birthdayreminder.BirthdayReminder.*;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


import de.ubuntix.android.birthdayreminder.BirthdayReminder;
import de.ubuntix.android.birthdayreminder.R;
import de.ubuntix.android.birthdayreminder.database.Database;
import de.ubuntix.android.birthdayreminder.database.Preferences;
import de.ubuntix.android.birthdayreminder.model.Contact;
import de.ubuntix.android.birthdayreminder.model.DateOfBirth;
import de.ubuntix.android.birthdayreminder.model.RawContact;
import de.ubuntix.android.birthdayreminder.util.CalendarUtils;
import de.ubuntix.android.birthdayreminder.util.StringUtils;

public class BirthdayBroadcastReceiver extends BroadcastReceiver {

	private static final String TIMED = "timed";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getBooleanExtra(TIMED, false)) {
			notifyBirthdays(context);
		}

		start(context);
	}

	private static PendingIntent createPendingIntent(Context context) {
		Intent intent = new Intent(context, BirthdayBroadcastReceiver.class);
		intent.putExtra(TIMED, true);
		return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
	}

	public static void start(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = createPendingIntent(context);

		Preferences prefs = Preferences.getInstance(context);
		Time nextUpdateTime = prefs.getUpdateTime();

		Calendar wakeUpCal = Calendar.getInstance();
		wakeUpCal.set(Calendar.HOUR_OF_DAY, nextUpdateTime.getHours());
		wakeUpCal.set(Calendar.MINUTE, nextUpdateTime.getMinutes());
		wakeUpCal.set(Calendar.SECOND, wakeUpCal.getActualMinimum(Calendar.SECOND));
		wakeUpCal.set(Calendar.MILLISECOND, wakeUpCal.getActualMinimum(Calendar.MILLISECOND));

		if (wakeUpCal.before(Calendar.getInstance())) {
			wakeUpCal.add(Calendar.DAY_OF_MONTH, 1);
		}

		alarmManager.set(AlarmManager.RTC_WAKEUP, wakeUpCal.getTimeInMillis(), pendingIntent);
	}

	public static void stop(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = createPendingIntent(context);
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}

	public static void restart(Context context) {
		stop(context);
		start(context);
	}

	private void notifyBirthdays(Context context) {
		Calendar today = CalendarUtils.todaysCalendar();
		Database db = new Database(context.getContentResolver());

		Preferences prefs = Preferences.getInstance(context);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Resources res = context.getResources();

		List<Contact> contacts = db.getAllContacts();

		// calculate next birthdays
		SortedMap<Integer, List<String>> nextBirthdays = new TreeMap<Integer, List<String>>();
		for (Contact contact : contacts) {
			Integer timeSpanToNextBirthday = null;
			for (RawContact rawContact : contact.getRawContacts()) {
				for (DateOfBirth dateOfBirth : rawContact.getDatesOfBirth()) {
					int timeSpan = CalendarUtils.timeSpanToNextBirthday(today, dateOfBirth.getDate());

					if (timeSpanToNextBirthday == null || timeSpanToNextBirthday > timeSpan) {
						timeSpanToNextBirthday = timeSpan;
					}
				}
			}

			if (timeSpanToNextBirthday != null && timeSpanToNextBirthday <= prefs.getDaysBeforeReminder()) {
				List<String> infoNames = nextBirthdays.get(timeSpanToNextBirthday);
				if (infoNames == null) {
					infoNames = new ArrayList<String>();
					nextBirthdays.put(timeSpanToNextBirthday, infoNames);
				}
				infoNames.add(contact.getName());
			}
		}

		// collect all sentences for the notification
		List<String> notificationTexts = new ArrayList<String>();
		int countBirthdays = 0;
		for (Integer days : nextBirthdays.keySet()) {
			List<String> birthdayList = nextBirthdays.get(days);
			assert birthdayList != null;
			String names = StringUtils.join(birthdayList, ", ").toString();
			notificationTexts.add(getBirthdayText(res, days, names));
			countBirthdays += birthdayList.size();
		}

		// cancel all notifications (clear old once)
		notificationManager.cancelAll();

		// create new notification
		if (notificationTexts.size() > 0) {
			String titleText = String.format(res.getQuantityString(R.plurals.notificationTitle, countBirthdays), countBirthdays);

			Intent intent = new Intent(context, BirthdayReminder.class);
			PendingIntent pi = PendingIntent.getActivity(
					context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

			Notification.Builder builder = null;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
				builder = new Notification.Builder(context,CHANNEL_ID);
			}

			assert builder != null;
			builder.setContentIntent(pi);
			builder.setSmallIcon(R.drawable.balloons);
			builder.setTicker(titleText);
			builder.setContentText(StringUtils.join(notificationTexts,", "));

			if (countBirthdays > 1) {
				builder.setNumber(countBirthdays);
			}
			Notification notification = builder.getNotification();
			notificationManager.notify(0, notification);
		}
	}


	private String getBirthdayText(Resources res, int days, String names) {
		String text;
		switch (days) {
		case 0:
			text = String.format(res.getString(R.string.notificationText_today), names);
			break;
		case 1:
			text = String.format(res.getString(R.string.notificationText_tomorrow), names);
			break;
		default:
			text = String.format(res.getString(R.string.notificationText_other), days, names);
		}
		return text;
	}
}
