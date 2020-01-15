package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.transaction.TransactionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class.getName());

    private AccountStorage accountStorage;
    private TransactionStorage transactionStorage;

    public AccountService(AccountStorage accountStorage, TransactionStorage transactionStorage) {
        this.accountStorage = accountStorage;
        this.transactionStorage = transactionStorage;
    }

    public Account create(AccountCreationRequest request) {
        Account account = accountStorage.create(request.getNumber());
        if(request.getAmount().compareTo(BigDecimal.ZERO) > 0) {
          transactionStorage.create(accountStorage.get(1l), account, request.getAmount());
        }
        return account;
    }

    public Account get(long id) {
        return accountStorage.get(id);
    }

    public List<Account> getAll() {
        return accountStorage.getAll();
    }

}
