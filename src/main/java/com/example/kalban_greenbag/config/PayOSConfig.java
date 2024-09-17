package com.example.kalban_greenbag.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
public class PayOSConfig {

    private final Dotenv dotenv;

    public PayOSConfig() {
        this.dotenv = Dotenv.load();
    }

    @Bean
    public PayOS payOS() {
        String clientId = dotenv.get("PAYOS_CLIENT_ID");
        String apiKey = dotenv.get("PAYOS_API_KEY");
        String checksumKey = dotenv.get("PAYOS_CHECKSUM_KEY");

        return new PayOS(clientId, apiKey, checksumKey);
    }
}
