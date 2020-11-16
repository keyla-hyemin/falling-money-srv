package com.kkp.demo.handler.error;

public class NotExistFallingMoneyInfoException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "Not Exist Falling Money Info.";
    }
    public String getErrCode(){
        return "1001";
    }
}
