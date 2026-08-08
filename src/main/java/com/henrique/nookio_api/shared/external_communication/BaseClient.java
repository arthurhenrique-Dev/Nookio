package com.henrique.nookio_api.shared.external_communication;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public abstract class BaseClient {

    private final Integer apiId;
    private final ApiClient apiClient;

    protected BaseClient(Integer apiId, String serviceUrl) {
        this.apiId = apiId;
        this.apiClient = ApiClient.of(serviceUrl);
    }

    protected Integer apiId() {
        return apiId;
    }

    protected Map<HttpStatusCode, Object> request(HttpMethod method, String endpoint, Object body) {
        return apiClient.request(method, endpoint, body);
    }
}
