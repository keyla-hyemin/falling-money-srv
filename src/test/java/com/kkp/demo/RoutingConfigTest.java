package com.kkp.demo;

import com.kkp.demo.config.RedisConfig;
import com.kkp.demo.config.RoutingConfig;
import com.kkp.demo.handler.ApiErrorParserHandler;
import com.kkp.demo.handler.FallingMoneyHandler;
import com.kkp.demo.service.FallingMoneyService;
import com.kkp.demo.dto.FallingMoneyHistory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@WebFluxTest
@Slf4j
@Import({FallingMoneyHandler.class, RoutingConfig.class, RedisConfig.class,
        FallingMoneyService.class, ApiErrorParserHandler.class})
public class RoutingConfigTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ApplicationContext context;

    @Test
    @Order(1)
    @DisplayName("뿌리기 API 요청")
    public void test_startFallingMoney() {
        String responseBody = webTestClient.post()
                .uri("http://localhost:8080/falling/money")
                .header("X-USER-ID","ku341557")
                .header("X-ROOM-ID","kr210")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(Map.of("money", 10000, "personnel", 5)))
                .exchange()
                .expectStatus().isOk() // 응답이 200인지 확인
                .expectBody(String.class) // 리턴하는 객체가 String 클래스인지 확인
                .returnResult().getResponseBody();

        // token 값 확인
        log.info("token = {}", responseBody);
        assertThat(responseBody).isNotEmpty();
    }

    @Test
    @Order(2)
    @DisplayName("받기 API 요청")
    public void test_getFallingMoney() {
        Integer responseBody = webTestClient.get().uri(uriBuilder ->
                uriBuilder
                        .path("/falling/money")
                        .queryParam("token", "Lsu")
                        .build())
                .header("X-USER-ID","ku229111")
                .header("X-ROOM-ID","kr210")
                .exchange()
                .expectStatus().isOk() // 응답이 200인지 확인
                .expectBody(Integer.class) // 리턴하는 객체가 String 클래스인지 확인
                .returnResult().getResponseBody();

        // 받은 금액 확인
        log.info("money = {}", responseBody);
        assertThat(responseBody).isGreaterThan(-1);
    }

    @Test
    @Order(3)
    @DisplayName("조회 API 요청")
    public void test_getFallingMoneyHistory() {
        FallingMoneyHistory responseBody = webTestClient.get().uri(uriBuilder ->
                uriBuilder
                        .path("/falling/money/history")
                        .queryParam("token", "Iyb")
                        .build())
                .header("X-USER-ID","ku341557")
                .header("X-ROOM-ID","kr210")
                .exchange()
                .expectStatus().isOk() // 응답이 200인지 확인
                .expectBody(FallingMoneyHistory.class) // 리턴하는 객체가 FallingMoney 클래스인지 확인
                .returnResult().getResponseBody();

        // 뿌리기 이력 확인
        log.info("owner = {}", responseBody.getOwnerId());
        log.info("room = {}", responseBody.getRoomId());
        log.info("created = {}", responseBody.getCreated());
        log.info("amount = {}", responseBody.getAmount());
        log.info("fallingAmount = {}", responseBody.getFallingAmount());
        log.info("token = {}", responseBody.getToken());
        log.info("users = {}", responseBody.getUsers());

        assertThat(responseBody.getAmount()).isGreaterThan(0);
        assertThat(responseBody.getOwnerId()).isEqualTo("ku341557");

    }

    @Test
    public void test_FailExecutePostMethod() {
        webTestClient.post().uri("/falling/money/history").exchange()
                .expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    @DisplayName("받기 API 요청 실패 케이스")
    public void test_fail_case_getFallingMoney() {
        webTestClient.get().uri(uriBuilder ->
                uriBuilder
                        .path("/falling/money")
                        .queryParam("token", "WIi")
                        .build())
                .header("X-USER-ID","ku112734")
                .header("X-ROOM-ID","kr210")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
