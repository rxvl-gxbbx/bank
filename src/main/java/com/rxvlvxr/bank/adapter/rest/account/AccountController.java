package com.rxvlvxr.bank.adapter.rest.account;

import com.rxvlvxr.bank.adapter.rest.account.dto.TransferDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.account.TransferException;
import com.rxvlvxr.bank.app.api.account.TransferInbound;
import com.rxvlvxr.bank.app.api.security.SecurityException;
import com.rxvlvxr.bank.domain.user.UserPrinciple;
import com.rxvlvxr.bank.staging.errorutils.ErrorsUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/bank-api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final TransferInbound transferInbound;

    @PatchMapping("/{id}/transfer")
    public ResponseEntity<ResponseDto> transfer(@PathVariable("id") Long id, @AuthenticationPrincipal UserPrinciple userPrinciple, @RequestBody @Valid TransferDto request, BindingResult bindingResult) {
        validateAuth(id, userPrinciple.user().getAccount().getId());

        if (bindingResult.hasErrors())
            throw new TransferException(ErrorsUtils.mapToDto(bindingResult));

        transferInbound.execute(id, request.getAccountId(), request.getAmount());

        return new ResponseEntity<>(new ResponseDto("Transaction successful", LocalDateTime.now()), HttpStatus.OK);
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateAuth(Long pathId, Long fromId) {
        if (!pathId.equals(fromId))
            throw new SecurityException(new ResponseDto("Authentication validation failed!", LocalDateTime.now()));
    }

    @ExceptionHandler
    private ResponseEntity<ResponseDto> handleException(EntityNotFoundException e) {
        return new ResponseEntity<>(new ResponseDto(e.getMessage(), LocalDateTime.now()), BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorsDto> handleException(TransferException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }
}
