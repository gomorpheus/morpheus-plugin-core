package com.morpheusdata.views;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * This model will be provided from Morpheus-ui to the plugin controller. It contains the request/response
 * @param <T> Type of object in the model.
 */
public class ViewModel<T> {
	public T object;
	public ServletRequest request;
	public ServletResponse response;
	public Integer status = 200;

	static ViewModel<?> of(Object data) {
		ViewModel<Object> obj = new ViewModel<>();
		obj.object = data;
		return obj;
	}
}
