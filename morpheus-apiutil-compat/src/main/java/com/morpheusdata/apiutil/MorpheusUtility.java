package com.morpheusdata.apiutil;

import javax.json.*;
import java.io.StringReader;
import java.util.*;

public class MorpheusUtility {

	public static Map getConfigMap(String config) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(config));
			JsonObject object = jsonReader.readObject();
			jsonReader.close();
			map = toMap(object);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public static Map toMap(JsonObject object) {
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

	public static List<Object> toList(JsonArray array) {
		List<Object> list = new ArrayList<Object>();
		for (Object value : array) {
			if (value instanceof JsonArray) {
				value = toList((JsonArray) value);
			} else if (value instanceof JsonObject) {
				value = toMap((JsonObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public static Object getConfigProperty(String prop, String config) {
		Map configMap = getConfigMap(config);
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
