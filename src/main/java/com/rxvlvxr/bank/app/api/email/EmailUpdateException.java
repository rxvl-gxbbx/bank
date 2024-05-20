package com.rxvlvxr.bank.app.api.email;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailUpdateException extends RuntimeException {
    private ErrorsDto errors;

    public EmailUpdateException(ErrorsDto errors) {
        this.errors = errors;
    }
}
