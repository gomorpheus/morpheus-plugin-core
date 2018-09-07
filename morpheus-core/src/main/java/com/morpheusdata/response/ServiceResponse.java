package com.morpheusdata.response;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceResponse {
	Boolean success = false;
	String msg = null;
	Map<String,String> errors;
	Object data;

	ServiceResponse(Boolean success, String msg, Map<String,String> errors, Object data) {
		this.success = success;
		this.msg = msg;
		this.errors = errors;
		this.data = data;
	}


	static ServiceResponse error(String msg) {
		return new ServiceResponse(false, msg, null, null);
	}

	static ServiceResponse error(String msg, Map<String,String> errors) {
		return new ServiceResponse(false, msg, errors, null);
	}

	static ServiceResponse error(String msg, Map<String,String> errors, Object data) {
		return new ServiceResponse(false, msg, errors, data);
	}

	static ServiceResponse success(Object data, String msg) {
		return new ServiceResponse(true, msg, null, data);
	}

	static ServiceResponse success(Object data) {
		return new ServiceResponse(true, null, null, data);
	}

	static ServiceResponse success() {
		return new ServiceResponse(true, null, null, null);
	}

	public Map<String,Object> toMap() {
		return toMap(null);
	}

	public Map<String,Object> toMap(String dataKeyName) {
		Map<String,Object> returnMap = new LinkedHashMap<>();
		returnMap.put("success",success);
		returnMap.put("msg",msg);
		returnMap.put("errors",errors);
		returnMap.put( (dataKeyName != null ? dataKeyName : "data"),data);
		return returnMap;
	}


	public String toString() {
		return toMap().toString();
	}
}
