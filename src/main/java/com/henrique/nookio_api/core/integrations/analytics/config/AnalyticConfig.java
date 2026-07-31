package com.henrique.nookio_api.core.integrations.analytics.config;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;
import com.henrique.nookio_api.shared.external_communication.ApiClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AnalyticConfig {

    @Value("${API_ID}")
    private Integer apiId;
    private ApiClient analyticsClient;

    private static final String BASE_ENDPOINT = "external/core";
    private static final String AUDIT_LOG_ENDPOINT = BASE_ENDPOINT + "/audit_logs";

    public boolean senderAuditLogs(AuditLogData data) {
        return senderAuditLogs(List.of(data));
    }

    public boolean senderAuditLogs(Collection<AuditLogData> dataList) {
        Map<HttpStatusCode, Object> response = analyticsClient.request(
                HttpMethod.POST,
                AUDIT_LOG_ENDPOINT,
                Map.of(
                        "api_id", apiId,
                        "logs", dataList
                )
        );

        return response.keySet().stream().anyMatch(HttpStatusCode::is2xxSuccessful);
    }
}
