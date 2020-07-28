package com.morpheusdata.model;

/**
 * Add allowed sources to the Content-Security-Policy HTTP header.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy">Content Security Policy Header</a>
 * @author Mike Truso
 */
public class TabContentSecurityPolicy {

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
}
