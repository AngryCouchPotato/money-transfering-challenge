package com.challenge.moneytransferring.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class.getName());

    private AccountStorage accountStorage;

    public AccountService(AccountStorage accountStorage) {
        this.accountStorage = accountStorage;
    }

    public Account create(CreateAccountRequest request) {
        return accountStorage.create(request.getNumber(), request.getAmount());
    }

    public Account get(long id) {
        return accountStorage.get(id);
    }

    public List<Account> getAll() {
        return accountStorage.getAll();
    }

    public Account delete(Long id) {
        return accountStorage.remove(id);
    }
}
