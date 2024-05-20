package com.rxvlvxr.bank.app.impl.account;

import com.rxvlvxr.bank.app.api.account.AccountRepository;
import com.rxvlvxr.bank.app.api.account.FindAllAccountsInbound;
import com.rxvlvxr.bank.domain.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAllAccountsUseCase implements FindAllAccountsInbound {
    private final AccountRepository accountRepository;

    @Override
    public List<Account> execute() {
        return accountRepository.findAll();
    }
}
