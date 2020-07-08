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
	 * helper with html and error response.
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
