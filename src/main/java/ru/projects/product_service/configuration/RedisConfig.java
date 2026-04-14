package ru.projects.product_service.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class RedisConfig {
    // RedisConnectionFactory and RedisTemplate are provided by Spring Boot AutoConfiguration
    // using spring.data.redis.host / spring.data.redis.port from application.yml (env: REDIS_HOST, REDIS_PORT)
}