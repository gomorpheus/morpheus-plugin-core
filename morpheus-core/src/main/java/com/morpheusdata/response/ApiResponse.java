package com.morpheusdata.response;

import java.util.Map;

public class ApiResponse extends ServiceResponse {

    private Map<String,String> headers;
    private String content;
    private String errorCode;
    // Holds the parsed json map or array.
    // TODO: Add jackson or a java json lib.
    public Object results;

    public ApiResponse() {
        super();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
