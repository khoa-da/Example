package com.example.kalban_greenbag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {

    @Value("${client.id}")
    private String clientId;

    @Value("${api.key}")
    private String apiKey;

    @Value("${check.sum.key}")
    private String checksumKey;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
