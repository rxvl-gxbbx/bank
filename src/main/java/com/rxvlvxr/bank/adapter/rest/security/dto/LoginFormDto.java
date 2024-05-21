package com.rxvlvxr.bank.adapter.rest.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginFormDto {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
}
