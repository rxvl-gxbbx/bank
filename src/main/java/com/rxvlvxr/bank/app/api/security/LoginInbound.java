package com.rxvlvxr.bank.app.api.security;

public interface LoginInbound {
    String execute(String login, String password);
}
