package de.ubuntix.android.birthdayreminder.util;

import java.util.Iterator;

public class StringUtils {

	public static String[] split(String str, char sep) {
		int count = 1;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == sep) {
				count++;
			}
		}

		String[] result = new String[count];

		int resultIndex = 0;
		int startIndex = 0;
		int separatorIndex;
		while ((separatorIndex = str.indexOf(sep, startIndex)) != -1) {
			result[resultIndex++] = str.substring(startIndex, separatorIndex);
			startIndex = separatorIndex + 1;
		}
		result[resultIndex] = str.substring(startIndex, str.length());

		return result;
	}

	public static <T> CharSequence join(final Iterable<T> objs, final String delimiter) {
		Iterator<T> iter = objs.iterator();
		if (!iter.hasNext())
			return "";
		StringBuffer buffer = new StringBuffer(String.valueOf(iter.next()));
		while (iter.hasNext())
			buffer.append(delimiter).append(String.valueOf(iter.next()));
		return buffer;
	}
}
