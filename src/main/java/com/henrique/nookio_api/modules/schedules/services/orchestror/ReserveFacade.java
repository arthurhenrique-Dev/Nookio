package com.henrique.nookio_api.modules.schedules.services.orchestror;

import com.henrique.nookio_api.modules.schedules.dto.CustomerDetailsInfo;
import com.henrique.nookio_api.modules.schedules.dto.PaymentRequestDto;
import com.henrique.nookio_api.modules.schedules.dto.ReserveScheduleDto;
import com.henrique.nookio_api.modules.schedules.models.Schedule;
import com.henrique.nookio_api.modules.schedules.services.reserve.PayReserveService;
import com.henrique.nookio_api.modules.schedules.services.reserve.ReserveScheduleService;
import com.henrique.nookio_api.modules.users.models.User;
import com.henrique.nookio_api.modules.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReserveFacade {

    private final UserRepository userRepository;
    private final ReserveScheduleService reserveSerice;
    private final PayReserveService paymentService;

    public void execute(ReserveScheduleDto dto){

        Schedule schedule = reserveSerice.exec(dto);

        User user = userRepository.findById(dto.userId())
                .orElseThrow(RuntimeException::new);
        CustomerDetailsInfo customerDetailsInfo =
                CustomerDetailsInfo.builder()
                        .fullname(user.getFirstName() + " " + user.getLastName())
                        .email(user.getEmail())
                        .taxId(user.getCpf())
                        .phone(user.getPhoneNumber())
                        .build();

        PaymentRequestDto request = new PaymentRequestDto(customerDetailsInfo, dto.paymentDto());

        paymentService.exec(request, schedule);
    }
}
