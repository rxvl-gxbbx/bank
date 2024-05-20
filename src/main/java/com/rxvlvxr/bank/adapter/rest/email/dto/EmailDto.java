package com.rxvlvxr.bank.adapter.rest.email.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailDto {
    @Pattern(regexp = "^[A-Za-zА-ЯЁа-яё\\d.\\-]+@[A-Za-zА-ЯЁа-яё\\d.\\-]+$", message = "Email validation failed!")
    private String address;
}
