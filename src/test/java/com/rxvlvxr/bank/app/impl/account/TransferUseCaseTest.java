package com.rxvlvxr.bank.app.impl.account;

import com.rxvlvxr.bank.app.api.account.TransferException;
import com.rxvlvxr.bank.app.api.user.UserRepository;
import com.rxvlvxr.bank.domain.account.Account;
import com.rxvlvxr.bank.domain.email.Email;
import com.rxvlvxr.bank.domain.phone.Phone;
import com.rxvlvxr.bank.domain.user.User;
import com.rxvlvxr.bank.fw.spring.BankApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@SpringBootTest(classes = BankApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("integration-test")
@Transactional(readOnly = true)
class TransferUseCaseTest {
    private static final double AMOUNT = 123.123;
    private static final int CAPACITY = 5;
    private static final double INIT_DEPOSIT = 100_000_000;

    private final FindAllAccountsUseCase findAllAccountsUseCase;
    private final FindAccountByIdUseCase findAccountByIdUseCase;
    private final TransferUseCase transferUseCase;
    private final UserRepository userRepository;

    private List<Account> accounts;
    private List<User> users;

    @Autowired
    public TransferUseCaseTest(FindAllAccountsUseCase findAllAccountsUseCase, FindAccountByIdUseCase findAccountByIdUseCase, TransferUseCase transferUseCase, UserRepository userRepository) {
        this.findAllAccountsUseCase = findAllAccountsUseCase;
        this.findAccountByIdUseCase = findAccountByIdUseCase;
        this.transferUseCase = transferUseCase;
        this.userRepository = userRepository;
    }

    @BeforeAll
    @Transactional
    public void setUp() {
        users = new ArrayList<>();

        for (int i = 0; i < CAPACITY; i++) {
            User user = new User("Full Name Test" + i, LocalDate.now().minusYears(18 + i), "login" + i, "hashPassword" + i);

            Account account = new Account(INIT_DEPOSIT, LocalDateTime.now(), INIT_DEPOSIT, user);
            Phone phone = new Phone("8900123121" + i, LocalDateTime.now(), user);
            Email email = new Email("mail" + i + "@mail.ru", LocalDateTime.now(), user);

            user.setAccount(account);
            user.setPhones(Collections.singletonList(phone));
            user.setEmails(Collections.singletonList(email));

            users.add(user);
        }

        userRepository.saveAll(users);

        accounts = users.stream()
                .map(User::getAccount)
                .sorted(Comparator.comparingLong(Account::getId))
                .toList();
    }

    @AfterAll
    @Transactional
    public void clear() {
        userRepository.deleteAll(users);
    }

    @Test
    @Transactional
    void isSizeCorrect() {
        Assertions.assertEquals(CAPACITY, accounts.size());
    }

    @Test
    @Transactional
    void transferToSelfThrowsException() {
        Assertions.assertThrows(TransferException.class, () -> transferUseCase.execute(accounts.get(0).getId(), accounts.get(0).getId(), AMOUNT));
    }

    @Test
    @Transactional
    void transferMoneyLowerThanZeroThrowsException() {
        Assertions.assertThrows(TransferException.class, () -> transferUseCase.execute(accounts.get(0).getId(), accounts.get(1).getId(), (double) -1));
    }

    @Test
    @Transactional
    void transferMoneyGreaterThanBalanceThrowsException() {
        Assertions.assertThrows(TransferException.class, () -> transferUseCase.execute(accounts.get(0).getId(), accounts.get(1).getId(), INIT_DEPOSIT * 5));
    }

    @Test
    @Transactional
    void transferCorrectAmount() {
        transferUseCase.execute(accounts.get(0).getId(), accounts.get(1).getId(), AMOUNT);

        List<Account> accountsFromTable = getAccounts();

        Assertions.assertEquals(accounts.get(0).getAmount() - AMOUNT, accountsFromTable.get(0).getAmount());
        Assertions.assertEquals(accounts.get(1).getAmount() + AMOUNT, accountsFromTable.get(1).getAmount());
    }

    @Test
    @Transactional
    void transferFromOneToOther26TimesGetCorrectAmount() {
        final int transferCount = 26;

        for (int i = 0; i < transferCount; i++) {
            transferUseCase.execute(accounts.get(0).getId(), accounts.get(1).getId(), AMOUNT);
            transferUseCase.execute(accounts.get(1).getId(), accounts.get(0).getId(), AMOUNT);
        }

        List<Account> accountsFromTable = getAccounts();

        Assertions.assertEquals(accounts.get(0).getAmount(), accountsFromTable.get(0).getAmount());
        Assertions.assertEquals(accounts.get(1).getAmount(), accountsFromTable.get(1).getAmount());
    }

    @Test
    @Transactional
    void transferFromOneToOtherAccountIn10ParallelThreadsFor50TimesEachAndGetCurrentAmount() throws InterruptedException {
        final int transferCount = 50;
        final int threadCount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < transferCount; j++) {
                    transferUseCase.execute(accounts.get(0).getId(), accounts.get(1).getId(), AMOUNT);
                    transferUseCase.execute(accounts.get(1).getId(), accounts.get(0).getId(), AMOUNT);
                }

                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        List<Account> accountsFromTable = getAccounts();

        Assertions.assertEquals(accounts.get(0).getAmount(), accountsFromTable.get(0).getAmount());
        Assertions.assertEquals(accounts.get(1).getAmount(), accountsFromTable.get(1).getAmount());
    }

    @Test
    @Transactional
    void transferToAccountFromThreeDifferentAccounts25TimesEachAndGetCurrentAmount() throws InterruptedException {
        final int maxIndex = accounts.size() - 1;
        final int count = 25;
        CountDownLatch countDownLatch = new CountDownLatch(maxIndex);

        for (int i = 0; i < maxIndex; i++) {
            int from = i;
            new Thread(() -> {
                for (int j = 0; j < count; j++) {
                    transferUseCase.execute(accounts.get(from).getId(), accounts.get(maxIndex).getId(), AMOUNT);
                }

                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        Account account = findAccountByIdUseCase.execute(accounts.get(maxIndex).getId());

        Assertions.assertEquals(accounts.get(maxIndex).getAmount() + count * AMOUNT * maxIndex, account.getAmount(), 0.001);
    }

    @Test
    @Transactional
    void transfer50TimesInCycleFrom10ParallelThreadsAndGetCorrectAmountInEachAccount() throws InterruptedException {
        final int transferCount = 50;
        final int threadCount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int x = 0; x < threadCount; x++) {
            new Thread(() -> {
                for (int y = 0; y < transferCount; y++) {
                    for (int i = 0; i < CAPACITY; i++) {
                        for (int j = CAPACITY - 1; j >= 0; j--) {
                            if (i != j)
                                transferUseCase.execute(accounts.get(i).getId(), accounts.get(j).getId(), AMOUNT);
                        }
                    }
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        List<Account> accountsFromTable = getAccounts();

        for (int i = 0; i < CAPACITY; i++) {
            Assertions.assertEquals(accounts.get(i).getAmount(), accountsFromTable.get(i).getAmount());
        }
    }

    private List<Account> getAccounts() {
        List<Account> accountsFromTable = findAllAccountsUseCase.execute().stream()
                .sorted(Comparator.comparingLong(Account::getId))
                .toList();

        accountsFromTable = accountsFromTable.stream()
                .skip(accountsFromTable.size() - accounts.size())
                .collect(Collectors.toList());

        return accountsFromTable;
    }
}