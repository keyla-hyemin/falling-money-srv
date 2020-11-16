package com.kkp.demo.handler;

import com.kkp.demo.handler.error.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ApiErrorParserHandler {

    public static Mono<ServerResponse> handleError(Throwable t) {
        Object returnMsgObject;

        if (t instanceof FallingMoneyEmptyException) {
            returnMsgObject = new WebApiError(((FallingMoneyEmptyException) t).getCustomErrorMsg(), ((FallingMoneyEmptyException) t).getErrCode());
        } else if (t instanceof NotExistFallingMoneyInfoException) {
            returnMsgObject = new WebApiError(((NotExistFallingMoneyInfoException) t).getCustomErrorMsg(), ((NotExistFallingMoneyInfoException) t).getErrCode());
        } else if (t instanceof DBProcessingException) {
            returnMsgObject = new WebApiError(((DBProcessingException) t).getCustomErrorMsg(), ((DBProcessingException) t).getErrCode());
        } else if (t instanceof InaccessibleException) {
            returnMsgObject = new WebApiError(((InaccessibleException) t).getCustomErrorMsg(), ((InaccessibleException) t).getErrCode());
        } else if (t instanceof MismatchRoomIdException) {
            returnMsgObject = new WebApiError(((MismatchRoomIdException) t).getCustomErrorMsg(), ((MismatchRoomIdException) t).getErrCode());
        } else if (t instanceof AlreadyExistFallingMoneyHistoryException) {
            returnMsgObject = new WebApiError(((AlreadyExistFallingMoneyHistoryException) t).getCustomErrorMsg(), ((AlreadyExistFallingMoneyHistoryException) t).getErrCode());
        } else if (t instanceof ExpiredFallingMoneyException) {
            returnMsgObject = new WebApiError(((ExpiredFallingMoneyException) t).getCustomErrorMsg(), ((ExpiredFallingMoneyException) t).getErrCode());
        } else {
            returnMsgObject = new WebApiError("9999", "Unexpected Exception");
        }

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BodyInserters.fromObject(returnMsgObject));
    }
}
