package com.rxvlvxr.bank.app.api.user;

import java.time.LocalDate;

public interface CreateUserInbound {
    void execute(String fullName, LocalDate birthDate,
                 String login, String password,
                 Double amount, String phone, String email);
}
