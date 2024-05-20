package com.rxvlvxr.bank.app.impl.email;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.EmailRegistrationException;
import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.app.api.email.ValidateEmailInbound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidateEmailUseCase implements ValidateEmailInbound {
    private final EmailRepository emailRepository;

    @Override
    public void execute(String email) {
        if (emailRepository.existsByAddress(email))
            throw new EmailRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User with this email already exists!", LocalDateTime.now()))));
    }
}