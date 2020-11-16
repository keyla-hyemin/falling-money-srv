package com.kkp.demo.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash(value = "FallingMoney", timeToLive = 600L)
public class FallingMoney {
    @Id
    String token;
    List<Integer> money;

    @Builder
    public FallingMoney(String token, List<Integer> money) {
        this.token = token;
        this.money = money;
    }
}
