package com.rxvlvxr.bank.adapter.persistence.email;

import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.domain.email.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailRepositoryAdapter implements EmailRepository {
    private final EmailJpaRepository emailJpaRepository;

    @Override
    public void save(Email email) {
        emailJpaRepository.save(email);
    }

    @Override
    public Optional<Email> findById(Long id) {
        return emailJpaRepository.findById(id);
    }

    @Override
    public void delete(Email email) {
        emailJpaRepository.delete(email);
    }

    @Override
    public boolean existsByAddress(String address) {
        return emailJpaRepository.existsByAddress(address);
    }
}
