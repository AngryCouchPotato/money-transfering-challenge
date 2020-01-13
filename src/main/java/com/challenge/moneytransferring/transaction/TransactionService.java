package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.Account;
import com.challenge.moneytransferring.account.AccountStorage;

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
        validateRequest(request);
        Account from = accountStorage.get(request.getFrom());
        Account to = accountStorage.get(request.getTo());
        validateAmount(from, request.getAmount());
        synchronized ((from.getId() < to.getId() ? from : to)) {
            synchronized ((from.getId() < to.getId() ? to : from)) {
                from.setAmount(from.getAmount().subtract(request.getAmount()));
                to.setAmount(to.getAmount().add(request.getAmount()));
                return transactionStorage.create(from, to, request.getAmount());
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

    private void validateRequest(TransactionRequest request) {
        if (request.getFrom() == request.getTo()) {
            throw new IllegalArgumentException(String.format("Accounts have to be different. base account = %d, destination account = %d", request.getFrom(), request.getTo()));
        }
    }

    private void validateAmount(Account from, BigDecimal amount) {
        if(from.getAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException(String.format("Account with id = %d and number = %s doesn't contain enough money", from.getId(), from.getNumber()));
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(String.format("Amount (%d) cannot be less or equals 0.", amount.doubleValue()));
        }
    }

}
