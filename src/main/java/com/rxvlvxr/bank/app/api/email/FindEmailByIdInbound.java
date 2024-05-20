package com.rxvlvxr.bank.app.api.email;

import com.rxvlvxr.bank.domain.email.Email;

public interface FindEmailByIdInbound {
    Email execute(Long id);
}
