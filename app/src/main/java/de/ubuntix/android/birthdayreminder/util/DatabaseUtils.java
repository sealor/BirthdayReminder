package de.ubuntix.android.birthdayreminder.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import android.database.Cursor;
import android.util.Log;

public class DatabaseUtils {

	/**
	 * Returns a list of objects of the specified class. The database cursor is used for retrieving the objects. The
	 * default constructor is needed for instantiation. The fields will be filled with the help of reflection.
	 * 
	 * @param clazz
	 *            class of the result objects
	 * @param cursor
	 *            cursor for the result set
	 * @return list of objects
	 */
	public static <T> List<T> createList(Class<T> clazz, Cursor cursor) {
		List<T> list = new ArrayList<T>();

		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				T instance = clazz.newInstance();

				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (!cursor.isNull(i)) {
						Field field = clazz.getDeclaredField(cursor.getColumnName(i));
						field.setAccessible(true);

						field.set(instance, castType(field.getType(), cursor, i));
					}
				}
				list.add(instance);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}

		return list;
	}

	/**
	 * Returns a sorted map of objects of the specified class. The database cursor is used for retrieving the objects.
	 * The default constructor is needed for instantiation. The fields will be filled with the help of reflection.
	 * 
	 * @param clazz
	 *            class of the result objects
	 * @param sortColumn
	 *            column name for map key
	 * @param cursor
	 *            cursor for the result set
	 * @return sorted map of objects
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> SortedMap<T, U> createSortedMap(Class<U> clazz, String sortColumn, Cursor cursor) {
		SortedMap<T, U> sortedMap = new TreeMap<T, U>();

		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				U instance = clazz.newInstance();
				T key = null;

				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (!cursor.isNull(i)) {
						String columnName = cursor.getColumnName(i);
						Field field = clazz.getDeclaredField(columnName);
						field.setAccessible(true);

						Object attribute = castType(field.getType(), cursor, i);
						field.set(instance, attribute);

						if (columnName.equals(sortColumn)) {
							key = (T) attribute;
						}
					}
				}
				sortedMap.put(key, instance);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}

		return sortedMap;
	}

	/**
	 * Returns an indexed map of objects of the specified class. The database cursor is used for retrieving the objects.
	 * The default constructor is needed for instantiation. The fields will be filled with the help of reflection.
	 * 
	 * @param clazz
	 *            class of the result objects
	 * @param indexColumn
	 *            column name for map key
	 * @param cursor
	 *            cursor for the result set
	 * @return indexed map of objects
	 */
	@SuppressWarnings("unchecked")
	public static <T, U> Map<T, U> createIndexedMap(Class<U> clazz, String indexColumn, Cursor cursor) {
		Map<T, U> indexedMap = new LinkedHashMap<T, U>();

		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				U instance = clazz.newInstance();
				T key = null;

				for (int i = 0; i < cursor.getColumnCount(); i++) {
					if (!cursor.isNull(i)) {
						String columnName = cursor.getColumnName(i);
						Field field = clazz.getDeclaredField(columnName);
						field.setAccessible(true);

						Object attribute = castType(field.getType(), cursor, i);
						field.set(instance, attribute);

						if (columnName.equals(indexColumn)) {
							key = (T) attribute;
						}
					}
				}
				indexedMap.put(key, instance);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(e.getClass().getSimpleName(), e.getLocalizedMessage());
		}

		return indexedMap;
	}

	private static Object castType(Class<?> paramClazz, Cursor cursor, int colNumber) {
		Object obj = null;
		if (paramClazz == String.class) {
			obj = cursor.getString(colNumber);
		} else if (paramClazz == Integer.class || paramClazz == int.class) {
			obj = cursor.getInt(colNumber);
		} else if (paramClazz == Long.class || paramClazz == long.class) {
			obj = cursor.getLong(colNumber);
		} else if (paramClazz == Float.class || paramClazz == float.class) {
			obj = cursor.getFloat(colNumber);
		} else if (paramClazz == Double.class || paramClazz == double.class) {
			obj = cursor.getDouble(colNumber);
		}
		return obj;
	}
}
