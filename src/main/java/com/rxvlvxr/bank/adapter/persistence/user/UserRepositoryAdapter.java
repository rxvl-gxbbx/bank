package com.rxvlvxr.bank.adapter.persistence.user;

import com.rxvlvxr.bank.app.api.user.UserRepository;
import com.rxvlvxr.bank.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByLogin(String login) {
        return userJpaRepository.findByLogin(login);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userJpaRepository.findAll(specification, pageable);
    }

    @Override
    public boolean existsByLogin(String login) {
        return userJpaRepository.existsByLogin(login);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(user);
    }

    @Override
    public void deleteAll(List<User> users) {
        userJpaRepository.deleteAll(users);
    }

    @Override
    public void saveAll(List<User> users) {
        userJpaRepository.saveAll(users);
    }
}
