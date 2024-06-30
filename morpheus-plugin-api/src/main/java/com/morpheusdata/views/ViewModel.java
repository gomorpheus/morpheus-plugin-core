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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.morpheusdata.model.User;
import java.util.Map;

/**
 * This model will be provided from Morpheus-ui to the plugin controller. It contains the request/response
 * @param <T> Type of object in the model.
 */
public class ViewModel<T> {
	public T object;
	public ServletRequest request;
	public ServletResponse response;
	public Integer status = 200;
	public User user;
	public Map<String, Object> opts;

	/**
	 * Helper method to create a successful ViewModel of an object
	 * @param data The object to base the model off of
	 * @return a ViewModel
	 */
	static ViewModel<?> of(Object data) {
		ViewModel<Object> obj = new ViewModel<>();
		obj.object = data;
		return obj;
	}

}
