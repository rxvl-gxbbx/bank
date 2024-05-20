package com.rxvlvxr.bank.app.api.user;

import com.rxvlvxr.bank.domain.user.User;

public interface FindUserByIdInbound {
    User execute(Long id);
}
