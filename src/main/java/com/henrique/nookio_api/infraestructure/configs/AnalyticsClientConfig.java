package com.henrique.nookio_api.infraestructure.configs;

import com.henrique.nookio_api.shared.external_communication.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsClientConfig {

    @Value("${ANALYTICS_SERVICE_URL}")
    private String receiverUrl;

    @Bean
    public ApiClient analyticsApiClient() {
        return ApiClient.of(receiverUrl);
    }
}