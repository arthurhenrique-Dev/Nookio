package com.henrique.nookio_api.infraestructure.configs;

import com.henrique.nookio_api.shared.external_communication.ApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocationClientConfig {

    @Bean
    public ApiClient locationApiClient(){ return ApiClient.of("https://nominatim.openstreetmap.org/search");}
}
