package com.rxvlvxr.bank.app.api.email;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRegistrationException extends RuntimeException {
    private ErrorsDto errors;

    public EmailRegistrationException(ErrorsDto errors) {
        this.errors = errors;
    }
}
