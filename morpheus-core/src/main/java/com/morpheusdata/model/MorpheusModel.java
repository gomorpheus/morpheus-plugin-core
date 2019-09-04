package com.morpheusdata.model;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Base class for all Morpheus Model classes. This provides dirty checking capabilities for most base object representations
 * and common id property representations. All setter methods in a Morpheus class should call the markDirty method. So that
 * the underlying Context classes can perform differential updates of these models.
 *
 * @author David Estes
 */
public class MorpheusModel {

	/**
	 * Database reference Id of the Object. Typically not directly set.
	 */
	protected Long id;

	/**
	 * Internal property used to keep track of a list of dirty fields on the currently extended Model class.
	 */
	private LinkedHashMap<String,Object> dirtyProperties = new LinkedHashMap<>();

	/**
	 * Gets the uniquely generated ID from the database record stored via the Morpheus appliance.
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Used for setting the unique ID of the Pool Type. This should not be directly used.
	 * @param id unique identifer
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * All setters that are presented to the morpheus API should call markDirty with a string representation of the fieldname.
	 * This enables the {@link com.morpheusdata.core.MorpheusContext} to reconcile differences and perform differential updates.
	 * @param propertyName Name of the property that has been changed
	 * @param value The newly assigned value the property has been given.
	 *
	 */
	protected void markDirty(String propertyName, Object value) {
		dirtyProperties.put(propertyName,value);
	}

	/**
	 * Marks the corresponding Model clean from all changes. This resets all stored differential value changes.
	 */
	public void markClean() {
		dirtyProperties.clear();
	}

	/**
	 * Gets a list of all dirty properties / fields (things that have changes) on the corresponding model based on the
	 * used setters.
	 * @return A List of dirty fields by name
	 */
	public Set<String> getDirtyProperties() {
		return dirtyProperties.keySet();
	}

	/**
	 * Gets a Map containing all dirty properties as well as their newly assigned values.
	 * @return A Map containing all model changes on properties from the original object.
	 */
	public LinkedHashMap<String,Object> getDirtyPropertyValues() {
		return dirtyProperties;
	}

	/**
	 * @return A Map of all properties, similar to Groovy's getProperties()
	 */
	public HashMap<String, Object> getPropertiesMap()  {
		HashMap<String, Object> map = new HashMap<>();

		for(Class clazz = this.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				Object value = null;
				try {
					value = field.get(this);
				} catch (IllegalAccessException ignore) { }
				map.put(name, value);
			}
		}
		return map;
	}

}
