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
 * A response object that is passed back to morpheus-ui to render html.
 */
public class HTMLResponse {
	public String html;
	public Integer status = 200;

	/**
	 * Helper with empty response and 200 success.
	 * @return HTMLResponse success
	 */
	public static HTMLResponse success() {
		HTMLResponse res = new HTMLResponse();
		res.html = "";
		return res;
	}

	/**
	 * Helper with html response and 200 success.
	 * @param html to render
	 * @return HTMLResponse success
	 */
	public static HTMLResponse success(String html) {
		HTMLResponse res = new HTMLResponse();
		res.html = html;
		return res;
	}

	/**
	 * Helper with html response and 200 success.
	 * @param html to render
	 * @return HTMLResponse
	 */
	public static HTMLResponse error(String html) {
		HTMLResponse res = new HTMLResponse();
		res.html = html;
		res.status = 400;
		return res;
	}

	/**
	 * Helper with html and error response.
	 * @param html html to render
	 * @param status error status code
	 * @return HTMLResponse
	 */
	public static HTMLResponse error(String html, Integer status) {
		HTMLResponse res = new HTMLResponse();
		res.html = html;
		res.status = status;
		return res;
	}
}
