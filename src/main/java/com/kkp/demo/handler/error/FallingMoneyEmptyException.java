package com.kkp.demo.handler.error;

public class FallingMoneyEmptyException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "falling money is empty.";
    }
    public String getErrCode(){
        return "1002";
    }
}
