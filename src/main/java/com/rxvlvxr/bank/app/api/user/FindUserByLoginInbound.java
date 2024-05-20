package com.rxvlvxr.bank.app.api.user;

import com.rxvlvxr.bank.domain.user.User;

public interface FindUserByLoginInbound {
    User execute(String login);
}
