package com.rxvlvxr.bank.app.impl.phone;

import com.rxvlvxr.bank.app.api.phone.FindPhoneByIdInbound;
import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.domain.phone.Phone;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindPhoneByIdUseCase implements FindPhoneByIdInbound {
    private final PhoneRepository phoneRepository;

    @Override
    public Phone execute(Long id) {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phone NOT found!"));
    }
}
