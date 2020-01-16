package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.Account;
import com.challenge.moneytransferring.account.AccountStorage;
import com.challenge.moneytransferring.exception.InvalidAccountBalanceException;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {
    private AccountStorage accountStorage;
    private TransactionStorage transactionStorage;

    public TransactionService(AccountStorage accountStorage, TransactionStorage transactionStorage) {
        this.accountStorage = accountStorage;
        this.transactionStorage = transactionStorage;
    }

    public Transaction create(TransactionRequest request) {
        Account from = accountStorage.get(request.getFrom());
        Account to = accountStorage.get(request.getTo());
        synchronized ((from.getId() < to.getId() ? from : to)) {
          synchronized ((from.getId() < to.getId() ? to : from)) {
            validateAccount(from, request.getAmount());
            return transactionStorage.create(from.getId(), to.getId(), request.getAmount());
          }
        }
    }

    public Transaction get(long id) {
        return transactionStorage.get(id);
    }

    public List<Transaction> getAll() {
        return transactionStorage.getAll();
    }

    public List<Transaction> getAll(long accountId) {
        return transactionStorage.getAll(accountId);
    }

    public BigDecimal getBalance(Account account) {
      synchronized (account) {
        return transactionStorage.getBalance(account.getId());
      }
    }

    private void validateAccount(Account from, BigDecimal amount) {
        if(getBalance(from).compareTo(amount) < 0) {
            throw new InvalidAccountBalanceException(String.format("Account with id = %d and number = %s doesn't contain enough money", from.getId(), from.getNumber()));
        }
    }
}
