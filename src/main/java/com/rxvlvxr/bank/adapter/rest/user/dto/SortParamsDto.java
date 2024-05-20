package com.rxvlvxr.bank.adapter.rest.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SortParamsDto {
    @Pattern(regexp = "id|fullName|birthDate|number|address", message = "example: id, fullName, birthDate, number, address")
    private String field;
    @Pattern(regexp = "asc|desc", message = "example: asc, desc")
    private String direction;
}