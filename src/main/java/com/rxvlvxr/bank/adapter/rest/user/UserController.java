package com.rxvlvxr.bank.adapter.rest.user;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.user.dto.FilterDto;
import com.rxvlvxr.bank.adapter.rest.user.dto.FilterResponseDto;
import com.rxvlvxr.bank.adapter.rest.user.dto.UsersDto;
import com.rxvlvxr.bank.app.api.user.FindUsersByFiltersInbound;
import com.rxvlvxr.bank.app.api.user.SearchException;
import com.rxvlvxr.bank.domain.email.Email;
import com.rxvlvxr.bank.domain.phone.Phone;
import com.rxvlvxr.bank.domain.user.User;
import com.rxvlvxr.bank.staging.errorutils.ErrorsUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bank-api/users")
@RequiredArgsConstructor
public class UserController {
    private final ModelMapper modelMapper;
    private final FindUsersByFiltersInbound findUsersByFiltersInbound;

    @PostMapping("/search")
    public UsersDto searchByFilters(@RequestBody @Valid FilterDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new SearchException(ErrorsUtils.mapToDto(bindingResult));
        return toDto(findUsersByFiltersInbound.execute(toUser(request), request));
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private User toUser(FilterDto request) {
        String p = request.getPhone();
        String e = request.getEmail();
        User user = new User(request.getFullName(), request.getBirthDate());

        if (p != null)
            user.setPhones(Collections.singletonList(new Phone(p)));
        if (e != null)
            user.setEmails(Collections.singletonList(new Email(e)));

        return user;
    }

    private UsersDto toDto(List<User> users) {
        return new UsersDto(users.stream()
                .map((user) -> modelMapper.map(user, FilterResponseDto.class))
                .toList());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(SearchException e) {
        return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
    }
}