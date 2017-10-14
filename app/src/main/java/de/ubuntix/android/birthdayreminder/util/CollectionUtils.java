package de.ubuntix.android.birthdayreminder.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {

	public static <K, V> Map<K, V> createIndex(Collection<V> collection, String indexMember) {
		Map<K, V> map = new LinkedHashMap<K, V>();

		Field field = null;
		for (V value : collection) {
			if (field == null) {
				try {
					field = value.getClass().getDeclaredField(indexMember);
					field.setAccessible(true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}

			try {
				@SuppressWarnings("unchecked")
				K key = (K) field.get(value);
				map.put(key, value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static <K, V> Map<K, Set<V>> createSetIndex(Collection<V> collection, String indexMember) {
		Map<K, Set<V>> map = new LinkedHashMap<K, Set<V>>();

		Field field = null;
		for (V value : collection) {
			if (field == null) {
				try {
					field = value.getClass().getDeclaredField(indexMember);
					field.setAccessible(true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}

			try {
				@SuppressWarnings("unchecked")
				K key = (K) field.get(value);
				Set<V> valueSet = map.get(key);
				if (valueSet == null) {
					valueSet = new LinkedHashSet<V>();
					map.put(key, valueSet);
				}
				valueSet.add(value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public static <K, V> Map<K, List<V>> createListIndex(Collection<V> collection, String indexMember) {
		Map<K, List<V>> map = new LinkedHashMap<K, List<V>>();

		Field field = null;
		for (V value : collection) {
			if (field == null) {
				try {
					field = value.getClass().getDeclaredField(indexMember);
					field.setAccessible(true);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}

			try {
				@SuppressWarnings("unchecked")
				K key = (K) field.get(value);
				List<V> valueList = map.get(key);
				if (valueList == null) {
					valueList = new ArrayList<V>();
					map.put(key, valueList);
				}
				valueList.add(value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
