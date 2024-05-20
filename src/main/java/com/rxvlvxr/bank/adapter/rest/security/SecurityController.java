package com.rxvlvxr.bank.adapter.rest.security;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.security.dto.JwtDto;
import com.rxvlvxr.bank.adapter.rest.security.dto.LoginFormDto;
import com.rxvlvxr.bank.adapter.rest.user.dto.UserDto;
import com.rxvlvxr.bank.app.api.security.LoginInbound;
import com.rxvlvxr.bank.app.api.user.CreateUserInbound;
import com.rxvlvxr.bank.app.api.user.UserRegistrationException;
import com.rxvlvxr.bank.staging.errorutils.ErrorsUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/bank-api/security")
@RequiredArgsConstructor
public class SecurityController {
    private final CreateUserInbound createUserInbound;
    private final LoginInbound loginInbound;

    @PostMapping("/registration")
    public ResponseEntity<JwtDto> registration(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new UserRegistrationException(ErrorsUtils.mapToDto(bindingResult));

        createUserInbound.execute(
                userDto.getFullName(),
                userDto.getBirthDate(),
                userDto.getLogin(),
                userDto.getPassword(),
                userDto.getAmount(),
                userDto.getPhone(),
                userDto.getEmail());

        String jwtToken = loginInbound.execute(userDto.getLogin(), userDto.getPassword());

        return new ResponseEntity<>(new JwtDto(jwtToken), CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> performLogin(@RequestBody @Valid LoginFormDto loginFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new UserRegistrationException(ErrorsUtils.mapToDto(bindingResult));

        String jwtToken = loginInbound.execute(loginFormDto.getLogin(), loginFormDto.getPassword());

        return new ResponseEntity<>(new JwtDto(jwtToken), OK);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(UserRegistrationException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }
}
