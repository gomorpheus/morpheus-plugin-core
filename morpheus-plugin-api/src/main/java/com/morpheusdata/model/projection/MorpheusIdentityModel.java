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

package com.morpheusdata.model.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morpheusdata.model.MorpheusModel;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Base class for identity projection Morpheus Model classes. 
 * @author bdwheeler
 */
public class MorpheusIdentityModel extends MorpheusModel {

	static final String CONFIG_FIELD = "config";

	/**
	 * @return A Map of identity properties
	 */
	@JsonIgnore
	public HashMap<String, Object> getIdentityProperties()  {
		HashMap<String, Object> map = new HashMap<>();
		for(Class clazz = this.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields) {
				String name = field.getName();
				if(!CONFIG_FIELD.equals(name)) {
					Object value = null;
					try {
						value = field.get(this);
					} catch(IllegalAccessException ignore) { }
					map.put(name, value);
				}
			}
		}
		return map;
	}

}
