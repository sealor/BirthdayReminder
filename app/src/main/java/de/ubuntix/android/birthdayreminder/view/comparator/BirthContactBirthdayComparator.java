package de.ubuntix.android.birthdayreminder.view.comparator;

import java.util.Calendar;
import java.util.Comparator;

import de.ubuntix.android.birthdayreminder.util.CalendarUtils;
import de.ubuntix.android.birthdayreminder.view.helper.BirthContact;

public class BirthContactBirthdayComparator implements Comparator<BirthContact> {

	private Calendar today = CalendarUtils.todaysCalendar();

	@Override
	public int compare(BirthContact bc1, BirthContact bc2) {
		Calendar date1 = CalendarUtils.nextBirthday(this.today, bc1.getDateOfBirth().getDate());
		Calendar date2 = CalendarUtils.nextBirthday(this.today, bc2.getDateOfBirth().getDate());

		int timeSpan1 = CalendarUtils.timeSpanInDays(this.today, date1);
		int timeSpan2 = CalendarUtils.timeSpanInDays(this.today, date2);

		int timeDiff = timeSpan1 - timeSpan2;

		// this objects are only equal if they have the same address
		if (timeDiff != 0) {
			return timeDiff;
		} else {
			return System.identityHashCode(bc1) - System.identityHashCode(bc2);
		}
	}
}
