package com.example.waguwagu;

import com.example.waguwagu.domain.entity.DeliveryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class WaguwaguApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaguwaguApplication.class, args);
	}

//	@Bean
//	public RedisTemplate<?, ?> geoTemplate(RedisConnectionFactory connectionFactory) {
//		RedisTemplate<?, ?> template = new RedisTemplate<>();
//		template.setConnectionFactory(connectionFactory);
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(new ObjectMapper(), DeliveryRequest.class));
//
//		return template;
//	}
}
