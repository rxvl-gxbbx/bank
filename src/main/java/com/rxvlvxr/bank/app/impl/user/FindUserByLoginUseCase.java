package com.rxvlvxr.bank.app.impl.user;

import com.rxvlvxr.bank.app.api.user.FindUserByLoginInbound;
import com.rxvlvxr.bank.app.api.user.UserRepository;
import com.rxvlvxr.bank.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUserByLoginUseCase implements FindUserByLoginInbound {
    private final UserRepository userRepository;

    @Override
    public User execute(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User NOT found!"));
    }
}
