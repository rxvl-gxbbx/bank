package com.rxvlvxr.bank.adapter.persistence.account;

import com.rxvlvxr.bank.app.api.account.AccountRepository;
import com.rxvlvxr.bank.domain.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {
    private final AccountJpaRepository accountJpaRepository;

    @Override
    public List<Account> findAll() {
        return accountJpaRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountJpaRepository.findById(id);
    }

    @Override
    public void save(Account account) {
        accountJpaRepository.save(account);
    }
}
