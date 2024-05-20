package com.rxvlvxr.bank.app.impl.email;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.EmailRegistrationException;
import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.app.api.email.FindEmailByIdInbound;
import com.rxvlvxr.bank.app.api.email.UpdateEmailInbound;
import com.rxvlvxr.bank.domain.email.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdateEmailUseCase implements UpdateEmailInbound {
    private final EmailRepository emailRepository;
    private final FindEmailByIdInbound findEmailByIdInbound;

    @Transactional
    @Override
    public void execute(Long id, Email updated) {
        validateEmail(updated.getAddress());
        Email email = findEmailByIdInbound.execute(id);
        email.setAddress(updated.getAddress());
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
