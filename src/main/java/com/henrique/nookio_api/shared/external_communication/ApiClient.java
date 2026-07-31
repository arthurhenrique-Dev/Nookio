package com.henrique.nookio_api.shared.external_communication;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

public class ApiClient {

    private final RestClient client;

    private ApiClient(String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofSeconds(5).toMillis());

        this.client = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public static ApiClient of(String baseUrl) {
        return new ApiClient(baseUrl);
    }

    public Map<HttpStatusCode, Object> request(HttpMethod method, String uri, Object body) {
        RestClient.RequestBodySpec requestSpec = client.method(method).uri(uri);

        if (body != null) {
            requestSpec.body(body);
        }

        ResponseEntity<Object> response = requestSpec.retrieve().toEntity(Object.class);
        Object responseBody = response.getBody() != null ? response.getBody() : Collections.emptyMap();

        return Map.of(response.getStatusCode(), responseBody);
    }
}
