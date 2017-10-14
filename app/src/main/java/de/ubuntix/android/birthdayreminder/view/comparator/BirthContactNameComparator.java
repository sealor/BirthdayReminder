package de.ubuntix.android.birthdayreminder.view.comparator;

import java.util.Comparator;

import de.ubuntix.android.birthdayreminder.view.helper.BirthContact;

public class BirthContactNameComparator implements Comparator<BirthContact> {

	@Override
	public int compare(BirthContact bc1, BirthContact bc2) {
		String thisName = bc1.getContact().getName() == null ? "" : bc1.getContact().getName();
		String anotherName = bc2.getContact().getName() == null ? "" : bc2.getContact().getName();

		int stringDiff = thisName.compareTo(anotherName);

		if (stringDiff != 0) {
			return stringDiff;
		} else {
			return System.identityHashCode(bc1) - System.identityHashCode(bc2);
		}
	}
}
