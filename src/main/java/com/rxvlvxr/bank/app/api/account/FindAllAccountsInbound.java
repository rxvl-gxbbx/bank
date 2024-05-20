package com.rxvlvxr.bank.app.api.account;

import com.rxvlvxr.bank.domain.account.Account;

import java.util.List;

public interface FindAllAccountsInbound {
    List<Account> execute();
}
