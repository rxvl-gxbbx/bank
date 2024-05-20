package com.rxvlvxr.bank.app.api.phone;

import com.rxvlvxr.bank.domain.phone.Phone;

public interface CreatePhoneInbound {
    void execute(Phone phone);
}
