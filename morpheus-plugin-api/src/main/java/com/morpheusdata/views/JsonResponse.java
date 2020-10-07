package com.morpheusdata.views;

/**
 * A response object that is passed back to morpheus-ui to render json and response. Optionally strongly typed.
 * @param <T> Type of the data object, can be Map
 */
public class JsonResponse<T> {
	public T data;
	public Integer status = 200;

	/**
	 * Helper method to create a successful JsonResponse of an object
	 * @param data The object to base the model off of
	 * @return a ViewModel
	 */
	public static JsonResponse<?> of(Object data) {
		JsonResponse<Object> obj = new JsonResponse<Object>();
		obj.data = data;
		return obj;
	}
}
