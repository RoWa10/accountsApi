package com.challenge.accounts.controller;

import com.challenge.accounts.dto.AccountCreateRequest;
import com.challenge.accounts.dto.AccountEventCreateRequest;
import com.challenge.accounts.dto.AccountEventResource;
import com.challenge.accounts.dto.AccountNameChangeRequest;
import com.challenge.accounts.dto.AccountResource;
import com.challenge.accounts.dto.AccountStatisticsResponse;
import com.challenge.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/accounts", produces = APPLICATION_JSON_VALUE)
public class AccountsRestController {

    private final AccountService accountService;


    @Autowired
    AccountsRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/{accountName}")
    public ResponseEntity<AccountResource> getAccount(@NotEmpty @PathVariable("accountName") String accountName) {
        return ResponseEntity.ok(accountService.getByName(accountName));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResource> postAccount(@Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        return ResponseEntity.ok(accountService.createAccount(accountCreateRequest.getAccountName()));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateAccount(@Valid @RequestBody AccountNameChangeRequest accountNameChangeRequest) {
        if (accountNameChangeRequest.getOldAccountName().equalsIgnoreCase(accountNameChangeRequest.getNewAccountName())) {
            throw new IllegalArgumentException("New account name can not be the same as old account name");
        }
        accountService.updateAccountName(accountNameChangeRequest.getOldAccountName(), accountNameChangeRequest.getNewAccountName());
        return ResponseEntity.accepted().build();
    }

    @GetMapping(path = "/{accountName}/accountEvents")
    public ResponseEntity<List<AccountEventResource>> getAccountEvents(@NotEmpty @PathVariable("accountName") String accountName) {
        return ResponseEntity.ok(accountService.getAccountEventByAccountName(accountName));
    }

    @PostMapping(path = "/accountEvents" , consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity postAccountEvent(@Valid @RequestBody AccountEventCreateRequest accountEventCreateRequest) {
       accountService.createAccountEvent(accountEventCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{accountName}/accountEvents/statistics")
    public ResponseEntity<List<AccountStatisticsResponse>> getAccountEventsStatistics(@NotEmpty @PathVariable("accountName") String accountName) {
        return ResponseEntity.ok(accountService.getAccountStatistics(accountName));
    }
}
