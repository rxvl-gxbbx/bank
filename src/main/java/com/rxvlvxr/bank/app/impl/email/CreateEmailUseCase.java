package com.rxvlvxr.bank.app.impl.email;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.CreateEmailInbound;
import com.rxvlvxr.bank.app.api.email.EmailRegistrationException;
import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.domain.email.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateEmailUseCase implements CreateEmailInbound {
    private final EmailRepository emailRepository;

    @Transactional
    @Override
    public void execute(Email email) {
        validateEmail(email.getAddress());
        email.setCreatedAt(LocalDateTime.now());
        emailRepository.save(email);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateEmail(String address) {
        if (emailRepository.existsByAddress(address))
            throw new EmailRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User with this email already exists!", LocalDateTime.now()))));
    }
}
