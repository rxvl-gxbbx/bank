package com.rxvlvxr.bank.app.api.phone;

import com.rxvlvxr.bank.domain.phone.Phone;

import java.util.Optional;

public interface PhoneRepository {
    Optional<Phone> findById(Long id);

    void save(Phone phone);

    void delete(Phone phone);

    boolean existsByNumber(String number);
}
