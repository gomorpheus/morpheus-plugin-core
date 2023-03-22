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
