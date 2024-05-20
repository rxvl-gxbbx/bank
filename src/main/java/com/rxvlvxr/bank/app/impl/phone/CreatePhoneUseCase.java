package com.rxvlvxr.bank.app.impl.phone;

import com.rxvlvxr.bank.app.api.phone.CreatePhoneInbound;
import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.domain.phone.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePhoneUseCase implements CreatePhoneInbound {
    private final PhoneRepository phoneRepository;

    @Transactional
    @Override
    public void execute(Phone phone) {
        phone.setCreatedAt(LocalDateTime.now());
        phoneRepository.save(phone);
    }
}
