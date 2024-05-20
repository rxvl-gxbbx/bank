package com.rxvlvxr.bank.app.impl.account;

import com.rxvlvxr.bank.app.api.account.AccountRepository;
import com.rxvlvxr.bank.app.api.account.FindAccountByIdInbound;
import com.rxvlvxr.bank.app.api.account.IncreaseBalanceForAllAccountsInbound;
import com.rxvlvxr.bank.domain.account.Account;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("!integration-test")
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IncreaseBalanceForAllAccountsUseCase implements IncreaseBalanceForAllAccountsInbound {
    private static final Logger LOGGER = LoggerFactory.getLogger(IncreaseBalanceForAllAccountsUseCase.class);
    private final AccountRepository accountRepository;
    private final FindAccountByIdInbound findAccountByIdInbound;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    @Override
    public synchronized void execute() {
        LOGGER.info("Adding funds to an account");
        List<Account> accounts = accountRepository.findAll();

        double newBalance;
        double maxBalance;

        for (Account acc : accounts) {
            newBalance = acc.getAmount() * 1.05;
            maxBalance = acc.getInitDeposit() * 2.07;

            if (newBalance <= maxBalance) {
                Account account = findAccountByIdInbound.execute(acc.getId());
                account.setAmount(newBalance);
                accountRepository.save(account);
            }
        }
    }
}
