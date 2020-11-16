package com.kkp.demo.handler.error;

public class MismatchRoomIdException extends RuntimeException {
    public String getCustomErrorMsg() {
        return "roomId is not same as";
    }
    public String getErrCode(){
        return "1003";
    }
}

