package com.morpheusdata.response;

import java.util.LinkedHashMap;
import java.util.Map;

public class ServiceResponse {
	private static final String DEFAULT_ERROR_KEY = "error";
	private Boolean success = false;
	private String msg = null;
	private Map<String,String> errors = new LinkedHashMap<>();
	private Object data;

	public ServiceResponse() { }

	public ServiceResponse(Boolean success, String msg, Map<String,String> errors, Object data) {
		this.success = success;
		this.msg = msg;
		this.errors = errors;
		this.data = data;
	}

	public static ServiceResponse error() {
		return new ServiceResponse(false, null, null, null);
	}

	public static ServiceResponse error(String msg) {
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

	public static ServiceResponse success() {
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

	public boolean hasError(String key) {
		if(errors == null) return false;
		return errors.containsKey(key);
	}

	public boolean hasErrors() {
		if(errors == null) return false;
		return errors.size() > 0;
	}

	public String toString() {
		return toMap().toString();
	}

	// Getters & Setters
	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public void addError(String key, String value) {
		this.errors.put(key, value);
	}

	public void removeError(String key) {
		this.errors.remove(key);
	}

	public String getError(String key) {
		return this.errors.getOrDefault(key, null);
	}

	/**
	 * Provided for backwards compatibility with existing getError()
	 * @return
	 */
	public String getError() {
		return this.errors.getOrDefault(DEFAULT_ERROR_KEY, null);
	}

	/**
	 * Provided for backwards compatibility with existing setError(msg)
	 * @return
	 */
	public void setError(String value) {
		this.errors.put(DEFAULT_ERROR_KEY, value);
	}

}
