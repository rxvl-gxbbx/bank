package com.rxvlvxr.bank.adapter.rest.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtDto {
    private String accessToken;
    private String type = "Bearer";

    public JwtDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
