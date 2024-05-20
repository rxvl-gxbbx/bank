package com.rxvlvxr.bank.app.impl.security;

import com.rxvlvxr.bank.app.api.user.FindUserByLoginInbound;
import com.rxvlvxr.bank.domain.user.User;
import com.rxvlvxr.bank.domain.user.UserPrinciple;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankUserDetailsService implements UserDetailsService {
    private final FindUserByLoginInbound findUserByLoginInbound;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = findUserByLoginInbound.execute(username);
            return new UserPrinciple(user);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
