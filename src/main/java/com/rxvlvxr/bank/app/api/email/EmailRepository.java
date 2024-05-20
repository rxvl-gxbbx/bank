package com.rxvlvxr.bank.app.api.email;

import com.rxvlvxr.bank.domain.email.Email;

import java.util.Optional;

public interface EmailRepository {
    void save(Email email);

    Optional<Email> findById(Long id);

    void delete(Email email);

    boolean existsByAddress(String address);
}
