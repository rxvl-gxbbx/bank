package com.rxvlvxr.bank.app.api.security;

import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityException extends RuntimeException {
    private ResponseDto responseDto;

    public SecurityException(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }
}
