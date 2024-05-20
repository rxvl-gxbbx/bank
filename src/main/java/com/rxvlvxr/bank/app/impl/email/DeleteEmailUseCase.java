package com.rxvlvxr.bank.app.impl.email;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.DeleteEmailInbound;
import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.app.api.email.EmailUpdateException;
import com.rxvlvxr.bank.app.api.email.FindEmailByIdInbound;
import com.rxvlvxr.bank.domain.email.Email;
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
public class DeleteEmailUseCase implements DeleteEmailInbound {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteEmailUseCase.class);
    private final EmailRepository emailRepository;
    private final FindEmailByIdInbound findEmailByIdInbound;

    @Transactional
    @Override
    public void execute(Long id) {
        LOGGER.info("Attempting to delete an email");
        Email email = findEmailByIdInbound.execute(id);
        validateDeletingEmail(email);

        email.getUser().getEmails().remove(email);
        emailRepository.delete(email);
        LOGGER.info("Email - {} deleted successfully!", email.getAddress());
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateDeletingEmail(Email email) {
        if (email.getUser().getEmails().stream()
                .filter(val -> val != email)
                .allMatch(another -> another.getCreatedAt().isBefore(email.getCreatedAt())))
            throw new EmailUpdateException(new ErrorsDto(Collections.singletonList(new ResponseDto("Email delete failed!", LocalDateTime.now()))));
    }
}
