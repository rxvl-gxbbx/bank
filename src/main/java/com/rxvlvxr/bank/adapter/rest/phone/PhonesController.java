package com.rxvlvxr.bank.adapter.rest.phone;

import com.rxvlvxr.bank.adapter.rest.phone.dto.PhoneDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.phone.*;
import com.rxvlvxr.bank.app.api.security.SecurityException;
import com.rxvlvxr.bank.domain.phone.Phone;
import com.rxvlvxr.bank.domain.user.User;
import com.rxvlvxr.bank.domain.user.UserPrinciple;
import com.rxvlvxr.bank.staging.errorutils.ErrorsUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/bank-api/phones")
@RequiredArgsConstructor
public class PhonesController {
    private final ModelMapper modelMapper;
    private final CreatePhoneInbound createPhoneInbound;
    private final UpdatePhoneInbound updatePhoneInbound;
    private final DeletePhoneInbound deletePhoneInbound;
    private final ValidatePhoneInbound validatePhoneInbound;

    @PostMapping
    public ResponseEntity<ResponseDto> add(@AuthenticationPrincipal UserPrinciple userDetails, @RequestBody @Valid PhoneDto phoneDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new PhoneRegistrationException(ErrorsUtils.mapToDto(bindingResult));
        validatePhoneInbound.execute(phoneDto.getNumber());

        User user = userDetails.user();
        Phone phone = toPhone(phoneDto);
        phone.setUser(user);

        createPhoneInbound.execute(phone);

        return new ResponseEntity<>(new ResponseDto("Phone added successfully!", LocalDateTime.now()), CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrinciple userDetails, @RequestBody @Valid PhoneDto phoneDto, BindingResult bindingResult) {
        User user = userDetails.user();
        validateAuth(id, user);

        if (bindingResult.hasErrors())
            throw new PhoneUpdateException(ErrorsUtils.mapToDto(bindingResult));
        validatePhoneInbound.execute(phoneDto.getNumber());

        Phone phone = toPhone(phoneDto);
        phone.setUser(user);

        updatePhoneInbound.execute(id, phone);

        return new ResponseEntity<>(new ResponseDto("Phone updated successfully!", LocalDateTime.now()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrinciple userDetails) {
        validateAuth(id, userDetails.user());

        deletePhoneInbound.execute(id);

        return new ResponseEntity<>(new ResponseDto("Phone deleted successfully!", LocalDateTime.now()), HttpStatus.OK);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateAuth(Long id, User user) {
        if (user.getPhones().stream()
                .noneMatch(busyPhone -> busyPhone.getId() == id))
            throw new SecurityException(new ResponseDto("Authentication validation failed!", LocalDateTime.now()));
    }

    private Phone toPhone(PhoneDto phoneDto) {
        return modelMapper.map(phoneDto, Phone.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(PhoneUpdateException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }
}
