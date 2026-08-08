package com.henrique.nookio_api.infraestructure.microsservices.payment;

import com.henrique.nookio_api.infraestructure.microsservices.payment.dto.PaymentResponseDto;
import com.henrique.nookio_api.modules.schedules.dto.PaymentRequestDto;

import java.util.List;
import java.util.UUID;

public interface PaymentsPort {
    PaymentResponseDto processPayment(PaymentRequestDto request);
    void repay(List<UUID> payments);
}
