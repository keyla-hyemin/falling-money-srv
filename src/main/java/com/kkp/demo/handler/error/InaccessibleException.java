package com.kkp.demo.handler.error;

public class InaccessibleException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "this access is prohibited";
    }
    public String getErrCode(){
        return "1004";
    }
}

