package com.rxvlvxr.bank.app.api.user;

import com.rxvlvxr.bank.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByLogin(String username);

    Optional<User> findById(Long id);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    boolean existsByLogin(String login);

    void save(User user);

    void deleteAll(List<User> users);

    void saveAll(List<User> users);
}
