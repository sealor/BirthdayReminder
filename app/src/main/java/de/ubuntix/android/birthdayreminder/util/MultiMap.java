package de.ubuntix.android.birthdayreminder.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class MultiMap<K, V> extends LinkedHashMap<K, Set<V>> {

	private static final long serialVersionUID = 1L;

	public Set<V> putElement(K key, V value) {
		Set<V> set = get(key);
		if (set == null) {
			set = new LinkedHashSet<V>();
			put(key, set);
		}
		set.add(value);
		return set;
	};
}
