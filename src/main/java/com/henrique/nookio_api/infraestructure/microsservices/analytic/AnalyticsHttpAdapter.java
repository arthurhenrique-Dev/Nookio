package com.henrique.nookio_api.infraestructure.microsservices.analytic;

import com.henrique.nookio_api.core.audit_logs.model.AuditLogData;
import com.henrique.nookio_api.shared.external_communication.BaseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@ConditionalOnProperty(
        prefix = "clients.analytics",
        name = "transport",
        havingValue = "http",
        matchIfMissing = true
)
public class AnalyticsHttpAdapter extends BaseClient implements AnalyticsPort {

    private static final String AUDIT_LOG_ENDPOINT = "/audit_logs";

    public AnalyticsHttpAdapter(
            @Value("${clients.api-id}") Integer apiId,
            @Value("${clients.analytics.url}") String serviceUrl
    ) {
        super(apiId, serviceUrl);
    }

    @Override
    public boolean sendAuditLogs(Collection<AuditLogData> dataList) {
        Map<HttpStatusCode, Object> response = request(
                HttpMethod.POST,
                AUDIT_LOG_ENDPOINT,
                Map.of(
                        "api_id", apiId(),
                        "logs", dataList
                )
        );

        return response.keySet().stream().anyMatch(HttpStatusCode::is2xxSuccessful);
    }
}
