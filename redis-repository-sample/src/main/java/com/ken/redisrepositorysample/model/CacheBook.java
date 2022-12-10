package com.ken.redisrepositorysample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "cache-book", timeToLive = 600)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheBook implements Serializable {

    @Id
    private Long userId;

    private String name;

    private String author;
}
