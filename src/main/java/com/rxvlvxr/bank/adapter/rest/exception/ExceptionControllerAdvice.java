package com.rxvlvxr.bank.adapter.rest.exception;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.EmailRegistrationException;
import com.rxvlvxr.bank.app.api.phone.PhoneRegistrationException;
import com.rxvlvxr.bank.app.api.security.SecurityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    private ResponseEntity<ResponseDto> handleException(SecurityException e) {
        return new ResponseEntity<>(new ResponseDto("Authentication validation failed", LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(PhoneRegistrationException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(EmailRegistrationException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }
}
