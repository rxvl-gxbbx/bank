package com.rxvlvxr.bank.adapter.rest.user.dto;

import com.rxvlvxr.bank.adapter.rest.account.dto.AccountDto;
import com.rxvlvxr.bank.adapter.rest.email.dto.EmailDto;
import com.rxvlvxr.bank.adapter.rest.phone.dto.PhoneDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FilterResponseDto {
    private String fullName;
    private LocalDate birthDate;
    private AccountDto account;
    private List<PhoneDto> phones;
    private List<EmailDto> emails;
}
