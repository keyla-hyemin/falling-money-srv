package com.kkp.demo.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@RedisHash(value = "FallingMoneyInfo", timeToLive = 604800L)
public class FallingMoneyInfo implements Serializable {
    @Id
    String token;
    String ownerId;
    String roomId;
    LocalDateTime created;
    int amount;

    @Builder
    public FallingMoneyInfo(String token, String ownerId, String roomId,
                            LocalDateTime created, int amount) {
        this.token = token;
        this.ownerId = ownerId;
        this.roomId = roomId;
        this.created = created;
        this.amount = amount;
    }
}
