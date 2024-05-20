package com.rxvlvxr.bank.app.api.account;

import com.rxvlvxr.bank.domain.account.Account;

public interface FindAccountByIdInbound {
    Account execute(Long id);
}
