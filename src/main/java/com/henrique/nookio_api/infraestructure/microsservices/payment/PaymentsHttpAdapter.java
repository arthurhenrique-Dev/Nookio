package com.henrique.nookio_api.infraestructure.microsservices.payment;

import com.henrique.nookio_api.infraestructure.microsservices.payment.dto.PaymentResponseDto;
import com.henrique.nookio_api.modules.schedules.dto.PaymentRequestDto;
import com.henrique.nookio_api.shared.external_communication.BaseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        prefix = "clients.payments",
        name = "transport",
        havingValue = "http",
        matchIfMissing = true
)
public class PaymentsHttpAdapter extends BaseClient implements PaymentsPort {

    public PaymentsHttpAdapter(
            @Value("${clients.api-id:1}") Integer apiId,
            @Value("${clients.payments.url}") String serviceUrl
    ) {
        super(apiId, serviceUrl);
    }

    @Override
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        Map<HttpStatusCode, Object> response = request(HttpMethod.POST, "/payments", request);

        if (response != null && !response.isEmpty()) {
            HttpStatusCode statusCode = response.keySet().iterator().next();
            if (statusCode.is2xxSuccessful()) {
                return new PaymentResponseDto(UUID.randomUUID(), "APPROVED", "Pagamento aprovado com sucesso.");
            }
        }
        return new PaymentResponseDto(null, "FAILED", "Falha ao processar pagamento no microsserviço.");
    }

    @Override
    public void repay(List<UUID> request) {
        request(HttpMethod.DELETE, "/payments", request);
    }
}
