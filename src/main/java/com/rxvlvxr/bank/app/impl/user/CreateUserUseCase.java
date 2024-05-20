package com.rxvlvxr.bank.app.impl.user;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.email.EmailRegistrationException;
import com.rxvlvxr.bank.app.api.email.EmailRepository;
import com.rxvlvxr.bank.app.api.phone.PhoneRegistrationException;
import com.rxvlvxr.bank.app.api.phone.PhoneRepository;
import com.rxvlvxr.bank.app.api.user.CreateUserInbound;
import com.rxvlvxr.bank.app.api.user.UserRegistrationException;
import com.rxvlvxr.bank.app.api.user.UserRepository;
import com.rxvlvxr.bank.domain.account.Account;
import com.rxvlvxr.bank.domain.email.Email;
import com.rxvlvxr.bank.domain.phone.Phone;
import com.rxvlvxr.bank.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateUserUseCase implements CreateUserInbound {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserUseCase.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;

    @Transactional
    @Override
    public void execute(String fullName, LocalDate birthDate, String login, String password, Double amount, String phone, String email) {
        LOGGER.info("Attempting to register a new user - {}", login);

        validateRegisteredAmount(amount);
        validateRegisteredLogin(login);
        validateRegisteredPhone(phone);
        validateRegisteredEmail(email);

        Account account = new Account(amount, LocalDateTime.now(), amount);
        Phone p = new Phone(phone, LocalDateTime.now());
        Email e = new Email(email, LocalDateTime.now());
        List<Phone> phones = List.of(p);
        List<Email> emails = List.of(e);

        User user = new User(fullName, birthDate, login, passwordEncoder.encode(password), account, phones, emails);
        account.setUser(user);
        p.setUser(user);
        e.setUser(user);

        userRepository.save(user);
        LOGGER.info("Registration of user - {} completed successfully", login);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateRegisteredPhone(String phone) {
        if (phoneRepository.existsByNumber(phone))
            throw new PhoneRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User with this phone already exists!", LocalDateTime.now()))));
    }

    private void validateRegisteredEmail(String email) {
        if (emailRepository.existsByAddress(email))
            throw new EmailRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User with this email already exists!", LocalDateTime.now()))));
    }

    private void validateRegisteredLogin(String login) {
        if (userRepository.existsByLogin(login))
            throw new UserRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User with this login already exists!", LocalDateTime.now()))));
    }

    private void validateRegisteredAmount(Double amount) {
        if (amount != null && amount <= 0)
            throw new UserRegistrationException(new ErrorsDto(Collections.singletonList(new ResponseDto("User validation failed!", LocalDateTime.now()))));
    }
}
