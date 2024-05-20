package com.rxvlvxr.bank.staging.errorutils;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ErrorsUtils {
    public static ErrorsDto mapToDto(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        ErrorsDto response = new ErrorsDto(new ArrayList<>());

        errors.forEach(error -> response.getErrors().add(new ResponseDto(error.getField() + " - " + error.getDefaultMessage(), LocalDateTime.now())));

        return response;
    }
}