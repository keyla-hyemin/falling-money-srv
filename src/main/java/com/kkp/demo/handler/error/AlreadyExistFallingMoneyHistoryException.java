package com.kkp.demo.handler.error;

public class AlreadyExistFallingMoneyHistoryException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "Already Exist Falling Money Info";
    }
    public String getErrCode(){
        return "1006";
    }
}
