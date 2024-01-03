package com.insert.view.domain.auth;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash
public class RefreshToken {
    @Id
    private String id;

    @Indexed
    private String refreshToken;

    @TimeToLive
    private long ttl;

    private ZonedDateTime expiredAt;

    public RefreshToken update(final String refreshToken, final long ttl) {
        this.refreshToken = refreshToken;
        this.ttl = ttl;
        return this;
    }
}
