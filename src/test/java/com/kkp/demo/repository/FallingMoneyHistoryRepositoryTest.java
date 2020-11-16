package com.kkp.demo.repository;

import com.kkp.demo.dto.FallingMoneyHistory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@Slf4j
@SpringBootTest
public class FallingMoneyHistoryRepositoryTest {
    @Autowired
    private FallingMoneyHistoryRepository fallingMoneyHistoryRepository;

    @Test
    @Order(1)
    @DisplayName("뿌리기 이력 적재")
    public void save_falling_money_history() {
        final FallingMoneyHistory fallingMoneyHistory = FallingMoneyHistory.builder().
                ownerId("ku341557").
                roomId("kr210").
                users(Map.of("ku532521", 1000)).
                amount(10000).
                token("ABC").
                created(LocalDateTime.now()).
                fallingAmount(0).
                build();
        final FallingMoneyHistory added = fallingMoneyHistoryRepository.save(fallingMoneyHistory);

        log.info("falling money {} fallingMoneyHistory", fallingMoneyHistory);
        log.info("falling money {} added", added.getToken());
    }

    @Test
    @Order(2)
    @DisplayName("뿌리기 이력 전체 조회")
    public void get_falling_money_history() {
       fallingMoneyHistoryRepository.findAll()
                .forEach(fallingMoneyHistory -> {
            log.info("owner = {}", fallingMoneyHistory.getOwnerId());
            log.info("room = {}", fallingMoneyHistory.getRoomId());
            log.info("created = {}", fallingMoneyHistory.getCreated());
            log.info("amount = {}", fallingMoneyHistory.getAmount());
            log.info("fallingAmount = {}", fallingMoneyHistory.getFallingAmount());
            log.info("token = {}", fallingMoneyHistory.getToken());
            log.info("users = {}", fallingMoneyHistory.getUsers());
                });
    }

    @Test
    @Order(2)
    @DisplayName("뿌리기 이력 조회")
    public void get_falling_money_history_by_id() {
        FallingMoneyHistory fallingMoneyHistory = fallingMoneyHistoryRepository.findById("Lsu").get();
        log.info("owner = {}", fallingMoneyHistory.getOwnerId());
        log.info("room = {}", fallingMoneyHistory.getRoomId());
        log.info("created = {}", fallingMoneyHistory.getCreated());
        log.info("amount = {}", fallingMoneyHistory.getAmount());
        log.info("falling amount = {}", fallingMoneyHistory.getFallingAmount());
        log.info("token = {}", fallingMoneyHistory.getToken());
        log.info("users = {}", fallingMoneyHistory.getUsers());
    }
}
