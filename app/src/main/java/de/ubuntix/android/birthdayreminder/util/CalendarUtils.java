package de.ubuntix.android.birthdayreminder.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

	private static final DateFormat androidDateformat = new SimpleDateFormat("yyyy-MM-dd");

	public static int timeSpanToNextBirthday(Calendar today, Calendar dayOfBirth) {
		Calendar nextBirthday = nextBirthday(today, dayOfBirth);
		return timeSpanInDays(today, nextBirthday);
	}

	public static int timeSpanInDays(Calendar cal1, Calendar cal2) {
		return (int) ((Math.abs(cal1.getTimeInMillis() - cal2.getTimeInMillis())) / (1000 * 60 * 60 * 24));
	}

	public static Calendar nextBirthday(Calendar today, Calendar dayOfBirth) {
		Calendar nextBirthday = (Calendar) dayOfBirth.clone();
		nextBirthday.set(Calendar.YEAR, today.get(Calendar.YEAR));
		if (nextBirthday.before(today)) {
			nextBirthday.add(Calendar.YEAR, 1);
		}
		return nextBirthday;
	}

	public static Calendar createNewCalendar(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, day);
		return calendar;
	}

	public static Calendar todaysCalendar() {
		Calendar now = Calendar.getInstance();
		return createNewCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
	}

	public static Calendar tomorrowsCalendar() {
		Calendar tomorrow = todaysCalendar();
		tomorrow.add(Calendar.DAY_OF_MONTH, 1);
		return tomorrow;
	}

	public static Calendar parseCalendar(String calendarString) {
		// quirk for birthdays with wrong format (yy instead of yyyy, e.g. google+)
		if (calendarString.matches("[0-9]{2}-[0-9]{2}-[0-9]{2}")) {
			calendarString = "19" + calendarString;
		}

		Date tmpDate;
		try {
			tmpDate = androidDateformat.parse(calendarString);
			Calendar parsedCalendar = Calendar.getInstance();
			parsedCalendar.clear();
			parsedCalendar.setTime(tmpDate);

			return parsedCalendar;
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatCalendar(Calendar calendar) {
		return androidDateformat.format(calendar.getTime());
	}
}
