package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.db.inmemory.Storage;
import com.challenge.moneytransferring.transaction.TransactionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

import static com.challenge.moneytransferring.util.Loggers.format;

public class AccountStorage extends Storage<Account> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionStorage.class.getName());

    private AtomicLong idCounter;

    public AccountStorage() {
        this.idCounter = new AtomicLong();
        create("Base account");
    }

    public Account create(String number) {
        Account account = new Account(nextId(), number);
        put(account.getId(), account);
        logger.info(format("Account was created. Id {}, number {}", account.getId(), account.getNumber()));
        return account;
    }

    public long nextId() {
        return idCounter.incrementAndGet();
    }
}
