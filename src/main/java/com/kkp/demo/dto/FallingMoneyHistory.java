package com.kkp.demo.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@RedisHash(value = "FallingMoneyHistory", timeToLive = 604800L)
public class FallingMoneyHistory implements Serializable {
    @Id
    String token;
    int fallingAmount;
    Map<String, Integer> users;
    String ownerId;
    String roomId;
    LocalDateTime created;
    int amount;

    @Builder
    public FallingMoneyHistory(String token, int fallingAmount,
                               Map<String, Integer> users, String ownerId, String roomId,
                               LocalDateTime created, int amount) {
        this.token = token;
        this.fallingAmount = fallingAmount;
        this.users = users;
        this.ownerId = ownerId;
        this.roomId = roomId;
        this.created = created;
        this.amount = amount;
    }
}
