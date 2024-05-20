package com.rxvlvxr.bank.adapter.rest.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PageParamsDto {
    @JsonProperty("page_number")
    @Min(1)
    private Integer pageNumber;
    @JsonProperty("page_size")
    @Min(1)
    private Integer pageSize;
}