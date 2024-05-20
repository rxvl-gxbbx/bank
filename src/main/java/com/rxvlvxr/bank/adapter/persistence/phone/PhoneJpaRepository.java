package com.rxvlvxr.bank.adapter.persistence.phone;

import com.rxvlvxr.bank.domain.phone.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneJpaRepository extends JpaRepository<Phone, Long> {
    boolean existsByNumber(String number);
}
