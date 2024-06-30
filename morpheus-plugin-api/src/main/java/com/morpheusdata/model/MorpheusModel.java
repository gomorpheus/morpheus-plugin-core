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

import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.*;
import java.util.*;
import javax.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all Morpheus Model classes. This provides dirty checking capabilities for most base object representations
 * and common id property representations. All setter methods in a Morpheus class should call the markDirty method. So that
 * the underlying Context classes can perform differential updates of these models.
 *
 * @author David Estes
 */
public class MorpheusModel {

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
				JsonReader jsonReader = Json.createReader(new StringReader(this.config));
				JsonObject object = jsonReader.readObject();
				jsonReader.close();
				map = toMap(object);
			}
		} catch(Exception e) {
			//fail silently
			log.error("Error parsing config as JSON: {}",e.getMessage(),e);
		}
		return map;
	}

	public void setConfigMap(Map<String, Object> map) {
		JsonObject object = mapToJson(map);
		setConfig(object.toString());
	}

	protected JsonObject mapToJson(Map<String, Object> map) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (String key : map.keySet()) {
			Object val = map.get(key);
			if (val instanceof String) {
				builder.add(key, (String) val);
			} else if (val instanceof Number) {
				if(val instanceof Integer) {
					builder.add(key, (Integer) val);
				} else if(val instanceof Double) {
					builder.add(key, (Double) val);
				} else if(val instanceof Long) {
					builder.add(key, (Long) val);
				} else if(val instanceof Boolean) {
					builder.add(key, (Boolean) val);
				} else if(val instanceof Short) {
					builder.add(key, (Short) val);
				} else if(val instanceof Float) {
					builder.add(key, (Float) val);
				} else {
					builder.add(key, (BigDecimal) val);
				}
			} else if(val instanceof Boolean) {
				builder.add(key, (Boolean) val);
			} else if(val instanceof Map) {
				builder.add(key, mapToJson((Map<String, Object>) val));
			} else if (val instanceof List) {
				builder.add(key, listToJson((List<Object>) val));
			} else {
				if(val != null) {
					builder.add(key, val.toString());
				} else {
					builder.addNull(key);
				}
			}
		}

		return builder.build();
	}

	private JsonArray listToJson(List<Object> list) {
		JsonArrayBuilder builder = Json.createArrayBuilder();

		for (Object val: list) {
			if (val instanceof String) {
				builder.add((String) val);
			} else if (val instanceof Number) {
				if(val instanceof Integer) {
					builder.add((Integer) val);
				} else if(val instanceof Double) {
					builder.add((Double) val);
				} else if(val instanceof Long) {
					builder.add((Long) val);
				} else if(val instanceof Boolean) {
					builder.add((Boolean) val);
				} else if(val instanceof Short) {
					builder.add((Short) val);
				} else if(val instanceof Float) {
					builder.add((Float) val);
				} else {
					builder.add((BigDecimal) val);
				}
			} else if(val instanceof Boolean) {
				builder.add((Boolean) val);
			} else if(val instanceof Map) {
				builder.add(mapToJson((Map<String, Object>) val));
			} else if (val instanceof List) {
				builder.add(listToJson((List<Object>) val));
			} else {
				if(val != null) {
					builder.add(val.toString());
				} else {
					builder.addNull();
				}
			}
		}

		return builder.build();
	}

	protected Map toMap(JsonObject object) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : object.keySet()) {
			Object val = null;
			JsonValue value = object.get(key);
			if (value instanceof JsonArray) {
				val = toList((JsonArray) value);
			} else if (value instanceof JsonObject) {
				val = toMap((JsonObject) value);
			} else if(value.getValueType() == JsonValue.ValueType.STRING) {
				val = object.getString(key);
			} else if(value.getValueType() == JsonValue.ValueType.NUMBER) {
				JsonNumber number = object.getJsonNumber(key);
				//val = number.isIntegral() ? number.longValue() : number.doubleValue();
				// HACK: Removed the use of the ternary operator because of what appears to be a bug in java/groovy
				// See Jordon for more explanation
				if (number.isIntegral())
					val = number.longValue();
				else
					val = number.doubleValue();
			} else if(value.getValueType() == JsonValue.ValueType.FALSE || value.getValueType() == JsonValue.ValueType.TRUE) {
				val = object.getBoolean(key);
			}
			map.put(key, val);
		}
		return map;
	}

	private List<Object> toList(JsonArray array) {
		List<Object> list = new ArrayList<Object>();
		for(int i = 0; i < array.size(); i++) {
			JsonValue value = array.get(i);
			Object val = new Object();
			if (value instanceof JsonArray) {
				val = toList((JsonArray) value);
			} else if (value instanceof JsonObject) {
				val = toMap((JsonObject) value);
			} else if(value.getValueType() == JsonValue.ValueType.STRING) {
				val = array.getString(i);
			} else if(value.getValueType() == JsonValue.ValueType.NUMBER) {
				JsonNumber number = array.getJsonNumber(i);
				val = number.isIntegral() ? number.longValue() : number.doubleValue();
			} else if(value.getValueType() == JsonValue.ValueType.FALSE || value.getValueType() == JsonValue.ValueType.TRUE) {
				val = array.getBoolean(i);
			}
			list.add(val);
		}
		return list;
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

	public void setConfigProperty(String prop, Object object) {
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
