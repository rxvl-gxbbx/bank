package com.rxvlvxr.bank.app.api.account;

import com.rxvlvxr.bank.domain.account.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository {
    List<Account> findAll();

    Optional<Account> findById(Long id);

    void save(Account account);
}
