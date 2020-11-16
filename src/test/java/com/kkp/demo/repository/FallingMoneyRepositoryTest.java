package com.kkp.demo.repository;

import com.kkp.demo.dto.FallingMoney;
import com.kkp.demo.handler.ApiErrorParserHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@Slf4j
@SpringBootTest
public class FallingMoneyRepositoryTest {
    @Autowired
    private FallingMoneyRepository fallingMoneyRepository;

    @Test
    @Order(1)
    @DisplayName("뿌리기 머니 적재")
    public void save_falling_money() {
        final FallingMoney fallingMoney = FallingMoney.builder().
                money(List.of(1000,2000,3000,4000,5000)).
                token("ABC").build();
        final FallingMoney added = fallingMoneyRepository.save(fallingMoney);

        log.info("falling money {} fallingMoney", fallingMoney);
        log.info("falling money {} added", added);
    }

    @Test
    @Order(2)
    @DisplayName("뿌리기 머니 조회")
    public void get_falling_money() {
        fallingMoneyRepository.findAll().forEach(fallingMoney -> {
            log.info("money = {}", fallingMoney.getMoney());
            log.info("token = {}", fallingMoney.getToken());
        });
    }

    @Test
    @Order(2)
    @DisplayName("뿌리기 머니 조회")
    public void get_falling_money_by_id() {
        FallingMoney fallingMoney = fallingMoneyRepository.findById("Lsu").get();
            log.info("money = {}", fallingMoney.getMoney());
            log.info("token = {}", fallingMoney.getToken());
    }
}
