package com.challenge.accounts.service;

import com.challenge.accounts.domain.Account;
import com.challenge.accounts.domain.AccountEvent;
import com.challenge.accounts.dto.AccountEventCreateRequest;
import com.challenge.accounts.dto.AccountEventResource;
import com.challenge.accounts.dto.AccountResource;
import com.challenge.accounts.dto.AccountStatisticsResponse;
import com.challenge.accounts.exception.AccountNotFoundException;
import com.challenge.accounts.repository.AccountEventRepository;
import com.challenge.accounts.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final AccountEventRepository accountEventRepository;

    @Autowired
    AccountServiceImpl(AccountRepository accountRepository, AccountEventRepository accountEventRepository) {
        this.accountRepository = accountRepository;
        this.accountEventRepository = accountEventRepository;
    }

    @Override
    public AccountResource getByName(String accountName) {
        Account account = accountRepository.getAccountByAccountName(accountName);
        if (Objects.isNull(account)) {
            throw new AccountNotFoundException("Account not found");
        }
        return buildAccountResource(account);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountResource createAccount(String accountName) {
        try {
            Account account = Account.builder()
                    .accountName(accountName)
                    .build();
            Account persistedAccount = accountRepository.saveAndFlush(account);
            return buildAccountResource(persistedAccount);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            LOGGER.error("Account creation failed with exception: {}", dataIntegrityViolationException.getMessage());
            throw new IllegalArgumentException("Invalid request, account already exists!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int updateAccountName(String oldAccountName, String newAccountName) {
        return accountRepository.updateAccountName(newAccountName, oldAccountName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAccountEvent(AccountEventCreateRequest request) {
        Account account = accountRepository.getAccountByAccountName(request.getAccountName());
        if (Objects.isNull(account)) {
             throw new AccountNotFoundException("No account found and hence no event created");
        }

        AccountEvent accountEvent = AccountEvent.builder()
                .accountEventType(request.getAccountEventType())
                .accountEventHappenedAt(request.getHappendAt().toInstant(ZoneOffset.UTC))
                .account(account)
                .build();

        accountEventRepository.saveAndFlush(accountEvent);
    }

    @Override
    public List<AccountEventResource> getAccountEventByAccountName(String accountName) {
        Account account = accountRepository.getAccountByAccountName(accountName);
        if (Objects.isNull(account)) {
            throw new IllegalArgumentException("No account event found");
        }
        List<AccountEvent> accountEvents = account.getAccountEvents();
        return accountEvents.stream().map(e -> AccountEventResource.builder()
                .eventType(e.getAccountEventType())
                .happenedAt(e.getAccountEventHappenedAt().toString())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<AccountStatisticsResponse> getAccountStatistics(String accountName) {
       return  accountEventRepository.countEventTypesByDay(accountName);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer invalidateOlderEvents(Instant validTillInstant) {
        return accountEventRepository.updateAccountEventExpired(validTillInstant);
    }


    private AccountResource buildAccountResource(Account account) {
        return AccountResource.builder()
                .accountIdentifier(account.getId().toString())
                .accountName(account.getAccountName())
                .accountCreationDate(account.getAccountCreatedAt().toString())
                .build();
    }
}
