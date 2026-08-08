package com.henrique.nookio_api.modules.schedules.services;

import com.henrique.nookio_api.infraestructure.microsservices.payment.PaymentsPort;
import com.henrique.nookio_api.modules.schedules.dto.ReserveScheduleDto;
import com.henrique.nookio_api.modules.schedules.models.Schedule;
import com.henrique.nookio_api.modules.schedules.models.ScheduleStatus;
import com.henrique.nookio_api.modules.schedules.repository.ScheduleRepository;
import com.henrique.nookio_api.modules.schedules.services.orchestror.ReserveFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulesService {

    private final ScheduleRepository repository;
    private final ReserveFacade reserveFacade;
    private final PaymentsPort paymentsPort;

    public void reserve(ReserveScheduleDto dto) {
        reserveFacade.execute(dto);
    }

    @Transactional
    public void checkIn(Integer scheduleId) {
        Schedule schedule = loadSchedule(scheduleId);
        if (schedule.getStatus() != ScheduleStatus.CONFIRMED) {
            throw new IllegalStateException("Apenas reservas confirmadas podem realizar check-in.");
        }
        if (schedule.getCheckIn() != null) {
            throw new IllegalStateException("Check-in já foi realizado anteriormente.");
        }
        schedule.setCheckIn(LocalDateTime.now());
        repository.save(schedule);
    }

    @Transactional
    public void checkout(Integer scheduleId) {
        Schedule schedule = loadSchedule(scheduleId);
        if (schedule.getCheckIn() == null) {
            throw new IllegalStateException("Check-in é obrigatório antes de realizar o check-out.");
        }
        if (schedule.getCheckOut() != null) {
            throw new IllegalStateException("Check-out já foi realizado anteriormente.");
        }
        schedule.setCheckOut(LocalDateTime.now());
        schedule.setStatus(ScheduleStatus.COMPLETED);
        repository.save(schedule);
    }

    @Transactional
    public void cancel(Integer scheduleId) {
        Schedule schedule = loadSchedule(scheduleId);
        schedule.setStatus(ScheduleStatus.CANCELLED);
        repository.save(schedule);
        if (LocalDate.now().plusDays(3).isBefore(schedule.getStart())) {
            paymentsPort.repay(List.of(schedule.getPaymentId()));
        }
    }

    private Schedule loadSchedule(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
    }
}
