package com.rxvlvxr.bank.app.api.phone;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneRegistrationException extends RuntimeException {
    private ErrorsDto errors;

    public PhoneRegistrationException(ErrorsDto errors) {
        this.errors = errors;
    }
}
