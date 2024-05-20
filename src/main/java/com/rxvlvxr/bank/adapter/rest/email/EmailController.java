package com.rxvlvxr.bank.adapter.rest.email;

import com.rxvlvxr.bank.adapter.rest.email.dto.EmailDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.*;
import com.rxvlvxr.bank.app.api.security.SecurityException;
import com.rxvlvxr.bank.domain.email.Email;
import com.rxvlvxr.bank.domain.user.User;
import com.rxvlvxr.bank.domain.user.UserPrinciple;
import com.rxvlvxr.bank.staging.errorutils.ErrorsUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/bank-api/emails")
@RequiredArgsConstructor
public class EmailController {
    private final ModelMapper modelMapper;
    private final CreateEmailInbound createEmailInbound;
    private final UpdateEmailInbound updateEmailInbound;
    private final DeleteEmailInbound deleteEmailInbound;
    private final ValidateEmailInbound validateEmailInbound;

    @PostMapping
    public ResponseEntity<ResponseDto> add(@AuthenticationPrincipal UserPrinciple userDetails, @RequestBody @Valid EmailDto emailDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new EmailRegistrationException(ErrorsUtils.mapToDto(bindingResult));
        validateEmailInbound.execute(emailDto.getAddress());

        User user = userDetails.user();
        Email email = toEmail(emailDto);
        email.setUser(user);

        createEmailInbound.execute(email);

        return new ResponseEntity<>(new ResponseDto("Email added successfully!", LocalDateTime.now()), CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrinciple userDetails, @RequestBody @Valid EmailDto emailDto, BindingResult bindingResult) {
        User user = userDetails.user();
        validateAuth(id, user);

        if (bindingResult.hasErrors())
            throw new EmailUpdateException(ErrorsUtils.mapToDto(bindingResult));
        validateEmailInbound.execute(emailDto.getAddress());

        Email email = toEmail(emailDto);
        email.setUser(user);

        updateEmailInbound.execute(id, email);

        return new ResponseEntity<>(new ResponseDto("Email updated successfully!", LocalDateTime.now()), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrinciple userDetails) {
        validateAuth(id, userDetails.user());
        deleteEmailInbound.execute(id);

        return new ResponseEntity<>(new ResponseDto("Email deleted successfully!", LocalDateTime.now()), OK);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateAuth(Long id, User user) {
        if (user.getEmails().stream()
                .noneMatch(busyMail -> busyMail.getId() == id))
            throw new SecurityException(new ResponseDto("Authentication validation failed!", LocalDateTime.now()));
    }

    private Email toEmail(EmailDto emailDto) {
        return modelMapper.map(emailDto, Email.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(EmailUpdateException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }
}
