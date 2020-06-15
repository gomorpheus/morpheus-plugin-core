package com.morpheusdata.views;

public class TemplateResponse {
	public String text;
	public Integer status = 200;

	public static TemplateResponse success() {
		TemplateResponse res = new TemplateResponse();
		res.text = "";
		return res;
	}

	public static TemplateResponse success(String message) {
		TemplateResponse res = new TemplateResponse();
		res.text = message;
		return res;
	}

	public static TemplateResponse error(String error) {
		TemplateResponse res = new TemplateResponse();
		res.text = error;
		res.status = 400;
		return res;
	}

	public static TemplateResponse error(String error, Integer status) {
		TemplateResponse res = new TemplateResponse();
		res.text = error;
		res.status = status;
		return res;
	}
}
