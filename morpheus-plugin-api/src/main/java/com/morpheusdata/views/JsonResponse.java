package com.morpheusdata.views;

public class JsonResponse<T> {
	public T data;
	public Integer status = 200;

	public static JsonResponse<?> of(Object data) {
		JsonResponse<Object> obj = new JsonResponse<Object>();
		obj.data = data;
		return obj;
	}
}
