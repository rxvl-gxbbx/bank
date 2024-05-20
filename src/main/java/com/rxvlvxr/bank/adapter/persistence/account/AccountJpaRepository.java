package com.rxvlvxr.bank.adapter.persistence.account;

import com.rxvlvxr.bank.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    List<Account> findAll();

    @Lock(PESSIMISTIC_WRITE)
    Optional<Account> findById(Long id);
}
