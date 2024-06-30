/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Api Parameters for some finders in the Context Services.
 * @author David Estes
 * @since 0.12.2
 * @param <K> The Key class type
 * @param <V> The Object class type (typically just Object)
 *
 */
public class ApiParameterMap<K, V> extends LinkedHashMap<K, V> {

	public ApiParameterMap() {
		super();
	}

	public ApiParameterMap(Map<K, V> params) {
		super();
		if(params != null) {
			this.putAll(params);
		}
	}

	public Collection<V> list(K key) throws ClassCastException {
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
