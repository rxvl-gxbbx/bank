package com.rxvlvxr.bank.app.impl.account;

import com.rxvlvxr.bank.app.api.account.AccountRepository;
import com.rxvlvxr.bank.app.api.account.FindAccountByIdInbound;
import com.rxvlvxr.bank.domain.account.Account;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindAccountByIdUseCase implements FindAccountByIdInbound {
    private final AccountRepository accountRepository;

    @Override
    public Account execute(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account NOT found!"));
    }
}
