package com.kkp.demo.handler;

import com.kkp.demo.service.FallingMoneyService;
import com.kkp.demo.dto.FallingMoneyRequestParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class FallingMoneyHandler {

  private static final String USER_ID_KEY = "X-USER-ID";
  private static final String ROOM_ID_KEY = "X-ROOM-ID";

  private final FallingMoneyService fallingMoneyService;

  private final ApiErrorParserHandler apiErrorParserHandler;

  public Mono<ServerResponse> startFallingMoney(ServerRequest serverRequest) {

    Mono<FallingMoneyRequestParam> fallingMoneyRequestParamMono = serverRequest.bodyToMono( FallingMoneyRequestParam.class );

    return fallingMoneyRequestParamMono.flatMap(fallingMoneyRequestParam ->
            fallingMoneyService.startFallingMoney(fallingMoneyRequestParam.getMoney(),
                    fallingMoneyRequestParam.getPersonnel(),
                    serverRequest.headers().asHttpHeaders().get(USER_ID_KEY).get(0),
                    serverRequest.headers().asHttpHeaders().get(ROOM_ID_KEY).get(0))
            .flatMap(m -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(m))))
            .onErrorResume(ApiErrorParserHandler::handleError);
  }

  /**
   * GET a FallingMoney
   */
  public Mono<ServerResponse> getFallingMoney(ServerRequest serverRequest) {
    return fallingMoneyService.getFallingMoney(serverRequest.queryParams().get("token").get(0),
            serverRequest.headers().asHttpHeaders().get(USER_ID_KEY).get(0),
            serverRequest.headers().asHttpHeaders().get(ROOM_ID_KEY).get(0))
            .flatMap(m -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(m)))
            .onErrorResume(ApiErrorParserHandler::handleError);
  }


  /**
   * GET a FallingMoneyHistory
   */
  public Mono<ServerResponse> getFallingMoneyHistory(ServerRequest serverRequest) {
    return fallingMoneyService.getFallingMoneyHistory(serverRequest.queryParams().get("token").get(0),
            serverRequest.headers().asHttpHeaders().get(USER_ID_KEY).get(0))
            .flatMap(m -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(m)))
            .onErrorResume(ApiErrorParserHandler::handleError);

  }
}
