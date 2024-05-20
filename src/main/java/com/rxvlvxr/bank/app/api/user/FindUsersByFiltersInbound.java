package com.rxvlvxr.bank.app.api.user;

import com.rxvlvxr.bank.adapter.rest.user.dto.FilterDto;
import com.rxvlvxr.bank.domain.user.User;

import java.util.List;

public interface FindUsersByFiltersInbound {
    List<User> execute(User user, FilterDto filterDto);
}
