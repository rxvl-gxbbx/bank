package com.rxvlvxr.bank.adapter.rest.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+$", message = "User validation failed!")
    @JsonProperty("full_name")
    private String fullName;
    @NotNull
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    @NotBlank
    private String login;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password validation failed!")
    private String password;
    @NotNull
    private Double amount;
    @NotBlank
    @Pattern(regexp = "^7\\d{10}$", message = "Validation failed! Example: 79001234567")
    private String phone;
    @NotBlank
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё\\d.\\-]+@[A-Za-zА-ЯЁа-яё\\d.\\-]+$", message = "Email validation failed!")
    private String email;
}
