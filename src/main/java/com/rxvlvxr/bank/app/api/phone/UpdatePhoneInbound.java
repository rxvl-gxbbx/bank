package com.rxvlvxr.bank.app.api.phone;

import com.rxvlvxr.bank.domain.phone.Phone;

public interface UpdatePhoneInbound {
    void execute(Long id, Phone updated);
}
