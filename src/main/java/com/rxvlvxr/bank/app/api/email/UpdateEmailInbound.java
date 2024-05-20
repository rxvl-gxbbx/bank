package com.rxvlvxr.bank.app.api.email;

import com.rxvlvxr.bank.domain.email.Email;

public interface UpdateEmailInbound {
    void execute(Long id, Email updated);
}
