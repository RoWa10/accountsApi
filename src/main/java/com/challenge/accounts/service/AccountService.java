package com.challenge.accounts.service;

import com.challenge.accounts.dto.AccountEventCreateRequest;
import com.challenge.accounts.dto.AccountEventResource;
import com.challenge.accounts.dto.AccountResource;
import com.challenge.accounts.dto.AccountStatisticsResponse;

import java.time.Instant;
import java.util.List;

public interface AccountService {

    AccountResource getByName(String accountName);

    AccountResource createAccount(String accountName);

    int updateAccountName(String oldAccountName, String newAccountName);

    void createAccountEvent(AccountEventCreateRequest request);

    List<AccountEventResource> getAccountEventByAccountName(String accountName);

    List<AccountStatisticsResponse> getAccountStatistics(String accountName);

    Integer invalidateOlderEvents(Instant validTillInstant);
}
