package com.adnan.cart.config;

import java.io.IOException;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
public class RedisServerEmbeddedConfig {
    
    @Value("${spring.redis.port}")
    private int redisPort;
    
    private RedisServer redisServer;
    
    @PostConstruct
    public void startRedis() throws IOException, URISyntaxException {
        redisServer = RedisServer.builder().port(redisPort).setting("maxmemory 128M").build();
        redisServer.start();
    }
    
    @PreDestroy
    public void stopRedis() throws InterruptedException {
        if (redisServer != null) {
            redisServer.stop();
        }
    }
    
}
