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

package com.morpheusdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Morpheus Model classes. This provides dirty checking capabilities for most base object representations
 * and common id property representations. All setter methods in a Morpheus class should call the markDirty method. So that
 * the underlying Context classes can perform differential updates of these models.
 *
 * @author David Estes
 */
public class MorpheusModel implements Serializable {

	static Logger log = LoggerFactory.getLogger(MorpheusModel.class);

	/**
	 * Database reference Id of the Object. Typically not directly set.
	 */
	protected Long id;

	protected String config;

	//TODO: Add validation errors property for all Models here. Think this should be a collection and not a map since
	//a key could have multiple errors.

	/**
	 * Internal property used to keep track of a list of dirty fields on the currently extended Model class.
	 */
	@JsonIgnore
	private LinkedHashMap<String,Object> dirtyProperties = new LinkedHashMap<>();

	/**
	 * Internal property used to keep track of a fields chages on the currently extended Model class.
	 */
	private LinkedHashMap<String,Object> persistedProperties = new LinkedHashMap<>();

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
	 * All setters that are presented to the morpheus API should call markDirty with a string representation of the fieldname.
	 * This enables the {@link com.morpheusdata.core.MorpheusContext} to reconcile differences and perform differential updates.
	 * @param propertyName Name of the property that has been changed
	 * @param value The newly assigned value the property has been given.
	 * @param persistedValue The old value of the property.
	 */
	protected void markDirty(String propertyName, Object value, Object persistedValue) {
		markDirty(propertyName, value);
		persistedProperties.put(propertyName, persistedValue);
	}

	/**
	 * Marks the corresponding Model clean from all changes. This resets all stored differential value changes.
	 */
	public void markClean() {
		dirtyProperties.clear();
		persistedProperties.clear();
	}

	/**
	 * Checks if a property has been modified
	 * @param propertyName Name of the property to check
	 * @return true or false
	 */
	public Boolean isDirty(String propertyName) {
		return dirtyProperties.containsKey(propertyName);
	}

	/**
	 * Gets a list of all dirty properties / fields (things that have changes) on the corresponding model based on the
	 * used setters.
	 * @return A List of dirty fields by name
	 */
	@JsonIgnore
	public Set<String> getDirtyProperties() {
		return dirtyProperties.keySet();
	}

	/**
	 * Gets a Map containing all dirty properties as well as their newly assigned values.
	 * @return A Map containing all model changes on properties from the original object.
	 */
	@JsonIgnore
	public LinkedHashMap<String,Object> getDirtyPropertyValues() {
		return dirtyProperties;
	}

	/**
	 * @return A Map of all properties, similar to Groovy's getProperties()
	 */
	@JsonIgnore
	public HashMap<String, Object> getProperties()  {
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

	public String getConfig() {
		return this.config;
	}

	public void setConfig(String config) {
		markDirty("config", config, this.config);
		this.config = config;
	}

	public Map getConfigMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(this.config != null && this.config != "") {
				TypeReference<Map<String,Object>> typeRef = new TypeReference<>() {
				};
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(this.config, typeRef);
			}
		} catch(Exception e) {
			//fail silently
			log.error("Error parsing config as JSON: {}",e.getMessage(),e);
		}
		return map;
	}

	public void setConfigMap(Map<String, Object> map) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String objString = mapper.writeValueAsString(map);
		setConfig(objString);
	}

	public Object getConfigProperty(String prop) {
		Map configMap = getConfigMap();
		Object propertyValue = null;
		if(!prop.contains(".")) {
			propertyValue = configMap.get(prop);
		} else {
			String[] parts = prop.split("\\.");
			Map nestedPart = configMap;
			for(String part : parts) {
				if(part.equals(parts[parts.length - 1])) {
					// last part, look for value
					propertyValue = nestedPart.get(part);
				} else {
					nestedPart = (Map) nestedPart.get(part);
				}
				if(nestedPart == null) {
					break;
				}
			}
		}
		return propertyValue;
	}

	public void setConfigProperty(String prop, Object object) throws JsonProcessingException {
		Map configMap = getConfigMap();
		if(!prop.contains(".")) {
			configMap.put(prop, object);
		} else {
			String[] parts = prop.split("\\.");
			Map nestedPart = configMap;
			for(String part : parts) {
				if(part.equals(parts[parts.length - 1])) {
					// last part, set value
					nestedPart.put(part, object);
				} else {
					nestedPart = (Map) nestedPart.get(part);
				}
				if(nestedPart == null) {
					break;
				}
			}
		}
		setConfigMap(configMap);
	}
}
