package com.henrique.nookio_api.modules.schedules.services.reserve;

import com.henrique.nookio_api.modules.schedules.dto.ReserveScheduleDto;
import com.henrique.nookio_api.modules.schedules.models.Schedule;
import com.henrique.nookio_api.modules.schedules.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Order(1)
@RequiredArgsConstructor
public class ReserveScheduleService {

    private final ScheduleRepository repository;

    public Schedule exec(ReserveScheduleDto context) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(15);

        boolean busy = repository.existsConflictingReservation(
                context.propertyId(), context.start(), context.end(), cutoffTime
        );

        if (busy) throw new IllegalArgumentException("Property not avaible for this period");

        Schedule schedule = Schedule.builder()
                .propertyId(context.propertyId())
                .guestId(context.userId())
                .ownerId(context.ownerId())
                .start(context.start())
                .end(context.end())
                .reservatedAt(LocalDateTime.now())
                .build();

        return repository.save(schedule);
    }
}
