package com.kkp.demo.handler.error;

public class DBProcessingException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "db error";
    }
    public String getErrCode(){
        return "1005";
    }
}

