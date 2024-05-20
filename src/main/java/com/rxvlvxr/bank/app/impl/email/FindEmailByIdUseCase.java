package com.rxvlvxr.bank.app.impl.email;

import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.app.api.email.FindEmailByIdInbound;
import com.rxvlvxr.bank.domain.email.Email;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindEmailByIdUseCase implements FindEmailByIdInbound {
    private final EmailRepository emailRepository;

    @Override
    public Email execute(Long id) {
        return emailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Email NOT found!"));
    }
}
