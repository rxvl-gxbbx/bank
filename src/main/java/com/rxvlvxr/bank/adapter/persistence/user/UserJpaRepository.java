package com.rxvlvxr.bank.adapter.persistence.user;

import com.rxvlvxr.bank.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByLogin(String username);

    boolean existsByLogin(String login);
}
