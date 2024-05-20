package com.rxvlvxr.bank.app.impl.account;

import com.rxvlvxr.bank.adapter.rest.response.dto.ErrorsDto;
import com.rxvlvxr.bank.adapter.rest.response.dto.ResponseDto;
import com.rxvlvxr.bank.app.api.account.AccountRepository;
import com.rxvlvxr.bank.app.api.account.FindAccountByIdInbound;
import com.rxvlvxr.bank.app.api.account.TransferException;
import com.rxvlvxr.bank.app.api.account.TransferInbound;
import com.rxvlvxr.bank.domain.account.Account;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferUseCase implements TransferInbound {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferUseCase.class);
    private final AccountRepository accountRepository;
    private final FindAccountByIdInbound findAccountByIdInbound;

    @Transactional
    @Override
    public synchronized void execute(Long fromId, Long toId, Double amount) {
        LOGGER.info("Attempting to transfer money");
        validateTransfer(fromId, toId);
        validateAmount(amount);

        Account accountFrom = findAccountByIdInbound.execute(fromId);

        validateAmount(amount, accountFrom.getAmount());

        Account accountTo = findAccountByIdInbound.execute(toId);

        accountFrom.setAmount(accountFrom.getAmount() - amount);
        accountTo.setAmount(accountTo.getAmount() + amount);

        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        LOGGER.info("Money transfer completed successfully!");
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private void validateAmount(Double amount) {
        if (amount <= 0)
            throw new TransferException(new ErrorsDto(Collections.singletonList(new ResponseDto("Transfer amount validation failed!", LocalDateTime.now()))));
    }

    private void validateAmount(Double amount, Double onBalance) {
        if (amount > onBalance)
            throw new TransferException(new ErrorsDto(Collections.singletonList(new ResponseDto("Not enough funds!", LocalDateTime.now()))));
    }

    private void validateTransfer(Long fromId, Long toId) {
        if (fromId.equals(toId))
            throw new TransferException(new ErrorsDto(Collections.singletonList(new ResponseDto("Transfer validation failed!", LocalDateTime.now()))));
    }
}
