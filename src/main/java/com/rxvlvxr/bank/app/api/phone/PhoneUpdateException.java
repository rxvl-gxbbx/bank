package com.rxvlvxr.bank.app.api.phone;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneUpdateException extends RuntimeException {
    private ErrorsDto errors;

    public PhoneUpdateException(ErrorsDto errors) {
        this.errors = errors;
    }
}
