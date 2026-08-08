package com.henrique.nookio_api.modules.schedules.services.reserve;

import com.henrique.nookio_api.infraestructure.microsservices.payment.PaymentsPort;
import com.henrique.nookio_api.infraestructure.microsservices.payment.dto.PaymentResponseDto;
import com.henrique.nookio_api.modules.schedules.dto.PaymentRequestDto;
import com.henrique.nookio_api.modules.schedules.models.Schedule;
import com.henrique.nookio_api.modules.schedules.models.ScheduleStatus;
import com.henrique.nookio_api.modules.schedules.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class PayReserveService {

    private final PaymentsPort paymentsPort;
    private final ScheduleRepository repository;

    public void exec(PaymentRequestDto context, Schedule schedule) {
        PaymentResponseDto response = paymentsPort.processPayment(context);
        repository.save(processPaymentResponse(schedule, response));
    }

    private Schedule processPaymentResponse(Schedule schedule, PaymentResponseDto response) {
        try {
            ScheduleStatus status = ScheduleStatus.fromString(response.status());
            schedule.setStatus(status);
            schedule.setPaymentId(response.paymentId());
        } catch (Exception e) {
            schedule.setStatus(ScheduleStatus.CANCELLED);
            if (response.paymentId() != null) paymentsPort.repay(List.of(response.paymentId()));
        }
        return schedule;
    }
}