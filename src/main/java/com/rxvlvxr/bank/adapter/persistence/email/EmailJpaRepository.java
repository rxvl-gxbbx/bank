package com.rxvlvxr.bank.adapter.persistence.email;

import com.rxvlvxr.bank.domain.email.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailJpaRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByAddress(String address);

    boolean existsByAddress(String address);
}
