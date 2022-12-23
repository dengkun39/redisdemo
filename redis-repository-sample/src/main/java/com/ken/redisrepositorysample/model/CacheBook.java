package com.ken.redisrepositorysample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "repository-book", timeToLive = 600)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheBook implements Serializable {

    @Id
    private Long userId;

    @Indexed

    private String name;

    private String author;
}
