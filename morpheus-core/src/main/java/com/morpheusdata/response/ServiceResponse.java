package com.morpheusdata.response;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


// generic
public class ServiceResponse<T> {
	private static final String DEFAULT_ERROR_KEY = "error";
	private Boolean success = false;
	private String msg = null;
	private Map<String,String> errors = new LinkedHashMap<>();
	private T data;
	private Map<String,Object> headers;
	private String content;
	private String errorCode;
	// Holds the parsed json map or array.
	// TODO: Add jackson or a java json lib.
	private Object results;
	private Map<String, String> cookies;

	public ServiceResponse() { }

	public ServiceResponse(Boolean success, String msg, Map<String,String> errors, T data) {
		this.success = success;
		this.msg = msg;
		if(errors != null) {
			this.errors = errors;
		}
		this.data = data;
	}

	public static ServiceResponse error() {
		ServiceResponse serviceResponse = new ServiceResponse(false, null, null, null);
		serviceResponse.setError("error");
		return serviceResponse;
	}

	public static ServiceResponse error(String msg) {
		ServiceResponse serviceResponse = new ServiceResponse(false, null, null, null);
		serviceResponse.setError(msg);
		return serviceResponse;
	}

	public static ServiceResponse error(String msg, Map<String,String> errors) {
		return new ServiceResponse(false, msg, errors, null);
	}

	public static ServiceResponse error(String msg, Map<String,String> errors, Object data) {
		return new ServiceResponse(false, msg, errors, data);
	}

	static ServiceResponse success(Object data, String msg) {
		return new ServiceResponse(true, msg, null, data);
	}

	public static ServiceResponse success(Object data) {
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
		if(!success) return true;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		if (errors != null) {
			this.success = false;
		}
		this.errors = errors;
	}

	public void addError(String value) {
		this.success = false;
		this.errors.put(DEFAULT_ERROR_KEY, value);
	}

	public void addError(String key, String value) {
		this.success = false;
		this.errors.put(key, value);
	}

	public void removeError() {
		this.errors.remove(DEFAULT_ERROR_KEY);
		if (this.errors.size() == 0) {
			this.success = true;
		}
	}

	public void clearErrors() {
		this.errors.clear();
		this.success = true;
	}

	public void removeError(String key) {
		this.errors.remove(key);
	}

	public String getError(String key) {
		return this.errors.getOrDefault(key, null);
	}

	/**
	 * Provided for backwards compatibility with existing getError()
	 * @return Error message
	 */
	public String getError() {
		return this.errors.getOrDefault(DEFAULT_ERROR_KEY, null);
	}

	/**
	 * Provided for backwards compatibility with existing setError(msg)
	 * @param value value to set
	 */
	public void setError(String value) {
		this.success = false;
		this.errors.put(DEFAULT_ERROR_KEY, value);
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public void addHeader(String key, Object value) {
		if(this.headers == null)
			this.headers = new HashMap<>();
		this.headers.put(key, value);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Object getResults() {
		return results;
	}

	public void setResults(Object results) {
		this.results = results;
	}

	public Map getCookies() {
		return cookies;
	}

	public void setCookies(Map cookies) {
		this.cookies = cookies;
	}

	public void addCookie(String key, Object value) {
		if(this.cookies == null)
			this.cookies = new HashMap<>();
		this.cookies.put(key, value.toString());
	}

	public String getCookie(String key) {
		if(this.cookies == null)
			return null;
		return this.cookies.getOrDefault(key, null);
	}
}
