package com.rxvlvxr.bank.app.api.user;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchException extends RuntimeException {
    private ErrorsDto errors;

    public SearchException(ErrorsDto errors) {
        this.errors = errors;
    }
}
