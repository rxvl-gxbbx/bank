package com.rxvlvxr.bank.adapter.rest.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FilterDto {
    @JsonProperty("full_name")
    private String fullName;
    @JsonProperty("birth_date")
    private LocalDate birthDate;
    private String phone;
    private String email;
    @Valid
    private PageParamsDto pagination;
    @Valid
    private SortParamsDto sort;
}
