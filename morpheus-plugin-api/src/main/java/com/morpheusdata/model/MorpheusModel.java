package com.morpheusdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import javax.json.*;

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

	protected String config;

	/**
	 * Internal property used to keep track of a list of dirty fields on the currently extended Model class.
	 */
	@JsonIgnore
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
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Map getConfigMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(this.config));
			JsonObject object = jsonReader.readObject();
			jsonReader.close();
			map = toMap(object);
		} catch(Exception e) {
			//fail silently
		}
		return map;
	}

	public void setConfigMap(Map<String, Object> map) {
		JsonObject object = mapToJson(map);
		this.config = object.toString();
	}

	private JsonObject mapToJson(Map<String, Object> map) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		for (String key : map.keySet()) {
			Object val = map.get(key);
			if (val instanceof String) {
				builder.add(key, (String) val);
			} else if (val instanceof Number) {
				if(val instanceof Integer) {
					builder.add(key, (Integer) val);
				} else {
					builder.add(key, (BigDecimal) val);
				}
			} else if(val instanceof Boolean) {
				builder.add(key, (Boolean) val);
			} else if(val instanceof Map) {
				builder.add(key, mapToJson((Map<String, Object>) val));
			} else {
				builder.add(key, val.toString());
			}
		}

		return builder.build();
	}

	private Map toMap(JsonObject object) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : object.keySet()) {
			Object val = new Object();
			JsonValue value = object.get(key);
			if (value instanceof JsonArray) {
				val = toList((JsonArray) value);
			} else if (value instanceof JsonObject) {
				val = toMap((JsonObject) value);
			} else if(value.getValueType() == JsonValue.ValueType.STRING) {
				val = object.getString(key);
			} else if(value.getValueType() == JsonValue.ValueType.NUMBER) {
				JsonNumber number = object.getJsonNumber(key);
				val = number.isIntegral() ? number.longValue() : number.doubleValue();
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
}
