package com.rxvlvxr.bank.adapter.rest.phone.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PhoneDto {
    @Pattern(regexp = "^7\\d{10}$", message = "Validation failed! Example: 79001234567")
    private String number;
}
