package com.rxvlvxr.bank.adapter.rest.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferDto {
    @NotNull
    private Long accountId;
    @NotNull
    @Min(0)
    private Double amount;
}
