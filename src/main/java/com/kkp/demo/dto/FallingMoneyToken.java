package com.kkp.demo.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "FallingMoneyToken", timeToLive = 600L)
public class FallingMoneyToken {
    @Id
    String token;

    @Builder
    public FallingMoneyToken(String token) {
        this.token = token;
    }
}
