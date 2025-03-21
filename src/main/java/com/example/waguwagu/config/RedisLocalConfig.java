package com.example.waguwagu.config;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Configuration
public class RedisLocalConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.afterPropertiesSet();
        RedisTemplate<String, String> geoTemplate = new RedisTemplate<>();
//        geoTemplate.setEnableTransactionSupport(true);
        geoTemplate.setConnectionFactory(connectionFactory);
        geoTemplate.setKeySerializer(new StringRedisSerializer());
        geoTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(new ObjectMapper(), DeliveryRequest.class));
        geoTemplate.afterPropertiesSet();
        return geoTemplate;
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(BytesToDateConverter converter) {
        return new RedisCustomConversions(Arrays.asList(converter));
    }
}
