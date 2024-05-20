package com.rxvlvxr.bank.app.api.email;

import com.rxvlvxr.bank.domain.email.Email;

public interface CreateEmailInbound {
    void execute(Email email);
}
