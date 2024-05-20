package com.rxvlvxr.bank.app.api.account;

import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferAuthException extends RuntimeException {
    private ResponseDto responseDto;

    public TransferAuthException(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
