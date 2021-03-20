package com.challenge.accounts.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String account_not_found) {
        super(account_not_found);
    }
}
