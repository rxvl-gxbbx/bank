package com.rxvlvxr.bank.adapter.persistence.phone;

import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.domain.phone.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhoneRepositoryAdapter implements PhoneRepository {
    private final PhoneJpaRepository phoneJpaRepository;

    @Override
    public Optional<Phone> findById(Long id) {
        return phoneJpaRepository.findById(id);
    }

    @Override
    public void save(Phone phone) {
        phoneJpaRepository.save(phone);
    }

    @Override
    public void delete(Phone phone) {
        phoneJpaRepository.delete(phone);
    }

    @Override
    public boolean existsByNumber(String number) {
        return phoneJpaRepository.existsByNumber(number);
    }
}
