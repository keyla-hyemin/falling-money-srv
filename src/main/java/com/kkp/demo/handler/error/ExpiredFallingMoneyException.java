package com.kkp.demo.handler.error;

public class ExpiredFallingMoneyException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "Expired Falling Money.";
    }
    public String getErrCode(){
        return "1007";
    }
}
