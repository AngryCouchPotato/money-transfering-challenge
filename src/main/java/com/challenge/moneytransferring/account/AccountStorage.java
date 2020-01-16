package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.db.inmemory.Storage;
import com.challenge.moneytransferring.transaction.TransactionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.challenge.moneytransferring.util.Loggers.format;

public class AccountStorage extends Storage<Account> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionStorage.class.getName());

    public AccountStorage() {
        create(Accounts.BASE_ACCOUNT_NAME);
    }

    public Account create(String number) {
        Account account = new Account(nextId(), number);
        put(account.getId(), account);
        logger.info(format("Account was created. Id {}, number {}", account.getId(), account.getNumber()));
        return account;
    }

}
