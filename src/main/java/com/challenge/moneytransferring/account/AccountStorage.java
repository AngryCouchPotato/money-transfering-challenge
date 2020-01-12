package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.db.inmemory.Storage;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class AccountStorage extends Storage<Account> {

    private AtomicLong idCounter;

    public AccountStorage() {
        this.idCounter = new AtomicLong();
    }

    public Account create(String number, BigDecimal amount) {
        Account account = new Account(nextId(), number, amount);
        put(account.getId(), account);
        return account;
    }

    public long nextId() {
        return idCounter.incrementAndGet();
    }
}
