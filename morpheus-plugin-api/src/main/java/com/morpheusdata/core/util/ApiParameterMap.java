package com.morpheusdata.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Api Parameters for some finders in the Context Services.
 * @author David Estes
 * @since 0.12.2
 * @param <K> The Key class type
 * @param <V> The Object class type (typically just Object)
 *
 */
public class ApiParameterMap<K,V> extends LinkedHashMap<K,V> {
	public ApiParameterMap() {
		super();
	}

	Collection<V> list(K key) throws ClassCastException {
		V value = get(key);
		if(value instanceof Collection) {
			return (Collection<V>) value;
		} else {
			ArrayList<V> myList = new ArrayList<V>();
			myList.add(value);
			return myList;
		}
	}

}
