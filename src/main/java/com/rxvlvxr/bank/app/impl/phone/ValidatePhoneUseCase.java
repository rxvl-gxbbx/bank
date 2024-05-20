package com.rxvlvxr.bank.app.impl.phone;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.phone.PhoneRegistrationException;
import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.app.api.phone.ValidatePhoneInbound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidatePhoneUseCase implements ValidatePhoneInbound {
    private final PhoneRepository phoneRepository;

    @Override
    public void execute(String phone) {
        if (phoneRepository.existsByNumber(phone))
            throw new PhoneRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User with this phone already exists!", LocalDateTime.now()))));
    }
}
