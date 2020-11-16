package com.kkp.demo.repository;

import com.kkp.demo.dto.FallingMoneyInfo;
import com.kkp.demo.dto.FallingMoneyToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@Slf4j
@SpringBootTest
public class FallingMoneyInfoRepositoryTest {
    @Autowired
    private FallingMoneyInfoRepository fallingMoneyInfoRepository;

    @Autowired
    private FallingMoneyTokenRepository fallingMoneyTokenRepository;
    @Test
    @Order(1)
    @DisplayName("뿌리기 정보 적재")
    public void save_falling_money_info() {
        final FallingMoneyInfo fallingMoneyInfo = FallingMoneyInfo.builder().
                ownerId("ku341557").
                roomId("kr210").
                amount(10000).
                token("ABC").
                created(LocalDateTime.now()).
                build();
        final FallingMoneyInfo added = fallingMoneyInfoRepository.save(fallingMoneyInfo);

        log.info("falling money {} fallingMoneyHistory", fallingMoneyInfo);
        log.info("falling money {} added", added.getToken());
    }

    @Test
    @Order(2)
    @DisplayName("뿌리기 정보 전체 조회")
    public void get_falling_money_info() {
       fallingMoneyInfoRepository.findAll()
                .forEach(fallingMoneyInfo -> {
            log.info("owner = {}", fallingMoneyInfo.getOwnerId());
            log.info("room = {}", fallingMoneyInfo.getRoomId());
            log.info("created = {}", fallingMoneyInfo.getCreated());
            log.info("amount = {}", fallingMoneyInfo.getAmount());
            log.info("token = {}", fallingMoneyInfo.getToken());
                });
    }

    @Test
    @Order(2)
    @DisplayName("뿌리기 정보 조회")
    public void get_falling_money_info_by_id() {
        FallingMoneyInfo fallingMoneyInfo = fallingMoneyInfoRepository.findById("Lsu").get();
        log.info("owner = {}", fallingMoneyInfo.getOwnerId());
        log.info("room = {}", fallingMoneyInfo.getRoomId());
        log.info("created = {}", fallingMoneyInfo.getCreated());
        log.info("amount = {}", fallingMoneyInfo.getAmount());
        log.info("token = {}", fallingMoneyInfo.getToken());
    }

    @Test
    @Order(3)
    @DisplayName("뿌리기 토큰 조회")
    public void get_falling_money_token_by_id() {
        FallingMoneyToken fallingMoneyToken = fallingMoneyTokenRepository.findById("Iyb").get();
        log.info("token = {}", fallingMoneyToken.getToken());
    }
}
