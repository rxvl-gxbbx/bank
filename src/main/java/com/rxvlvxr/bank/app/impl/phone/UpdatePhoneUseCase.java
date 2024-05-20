package com.rxvlvxr.bank.app.impl.phone;

import com.rxvlvxr.bank.app.api.phone.FindPhoneByIdInbound;
import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.app.api.phone.UpdatePhoneInbound;
import com.rxvlvxr.bank.domain.phone.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UpdatePhoneUseCase implements UpdatePhoneInbound {
    private final FindPhoneByIdInbound findPhoneByIdInbound;
    private final PhoneRepository phoneRepository;

    @Transactional
    @Override
    public void execute(Long id, Phone updated) {
        Phone phone = findPhoneByIdInbound.execute(id);
        phone.setNumber(updated.getNumber());
        phoneRepository.save(phone);
    }
}
