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
