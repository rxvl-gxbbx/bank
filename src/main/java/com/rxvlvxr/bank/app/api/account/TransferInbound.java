package com.rxvlvxr.bank.app.api.account;

public interface TransferInbound {
    void execute(Long fromId, Long toId, Double amount);
}
