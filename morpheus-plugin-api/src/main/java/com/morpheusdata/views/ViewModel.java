package com.morpheusdata.views;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ViewModel<T> {
	public T object;
	public ServletRequest request;
	public ServletResponse response;
	public Integer status = 200;

	static ViewModel<?> of(Object data) {
		ViewModel<Object> obj = new ViewModel<Object>();
		obj.object = data;
		return obj;
	}
}
