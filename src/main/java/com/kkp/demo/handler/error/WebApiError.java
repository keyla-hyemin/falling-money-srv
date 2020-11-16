package com.kkp.demo.handler.error;

import java.util.HashMap;

public class WebApiError {
    private String code;
    private String message;

    public WebApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
