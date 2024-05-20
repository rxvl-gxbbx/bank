package com.rxvlvxr.bank.app.impl.phone;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.phone.DeletePhoneInbound;
import com.rxvlvxr.bank.app.api.phone.FindPhoneByIdInbound;
import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.app.api.phone.PhoneUpdateException;
import com.rxvlvxr.bank.domain.phone.Phone;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeletePhoneUseCase implements DeletePhoneInbound {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePhoneUseCase.class);
    private final PhoneRepository phoneRepository;
    private final FindPhoneByIdInbound findPhoneByIdInbound;

    @Transactional
    @Override
    public void execute(Long id) {
        LOGGER.info("Attempting to delete a phone number");
        Phone phone = findPhoneByIdInbound.execute(id);
        validateDeletingPhone(phone);

        phone.getUser().getPhones().remove(phone);
        phoneRepository.delete(phone);
        LOGGER.info("Phone number - {} deleted successfully", phone.getNumber());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateDeletingPhone(Phone phone) {
        if (phone.getUser().getPhones().stream()
                .filter(val -> val != phone)
                .allMatch(another -> another.getCreatedAt().isBefore(phone.getCreatedAt())))
            throw new PhoneUpdateException(new ErrorsDto(Collections.singletonList(new ResponseDto("Phone delete failed!", LocalDateTime.now()))));
    }
}
