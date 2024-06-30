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

package com.morpheusdata.model;

/**
 * Add allowed sources to the Content-Security-Policy HTTP header.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy">Content Security Policy Header</a>
 * @author Mike Truso
 */
public class ContentSecurityPolicy {

	/**
	 * CSP frame-src directive
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/frame-src">frame-src directive</a>
	 */
	public String frameSrc;

	/**
	 * CSP img-src directive
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/img-src">img-src directive</a>
	 */
	public String imgSrc;

	/**
	 * CSP script-src directive
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/script-src">script-src directive</a>
	 */
	public String scriptSrc;

	/**
	 * CSP style-src directive
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/style-src">style-src directive</a>
	 */
	public String styleSrc;

	/**
	 * CSP connect-src directive
	 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/connect-src">style-src directive</a>
	 */
	public String connectSrc;
}
