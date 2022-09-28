package com.admin.tool.common.config.web;

import org.springframework.boot.web.client.ClientHttpRequestFactorySupplier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

        // 기본 헤더 등등 설정

        return restTemplateBuilder
                .requestFactory(new ClientHttpRequestFactorySupplier())
                .setConnectTimeout(Duration.ofMillis(5000)) //읽기시간초과, ms
                .setReadTimeout(Duration.ofMillis(5000))    //연결시간초과, ms
                .build();
    }
}
