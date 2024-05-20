package com.rxvlvxr.bank.app.api.account;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferException extends RuntimeException {
    private ErrorsDto errors;

    public TransferException(ErrorsDto errors) {
        this.errors = errors;
    }
}
