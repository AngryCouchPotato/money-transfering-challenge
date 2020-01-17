package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.db.inmemory.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.challenge.moneytransferring.util.Loggers.format;

public class TransactionStorage extends Storage<Transaction> {

    private static final Logger logger = LoggerFactory.getLogger(TransactionStorage.class.getName());


    private ConcurrentMap<Long, List<Transaction>> transactionsByAccountId;

    public TransactionStorage() {
        this.transactionsByAccountId = new ConcurrentHashMap<>();
    }

    public Transaction create(long fromAccountId, long toAccountId, BigDecimal amount) {
        Transaction transaction = new Transaction(nextId(), fromAccountId, toAccountId, amount, LocalDateTime.now());
        put(transaction.getId(), transaction);
        transactionsByAccountId.computeIfAbsent(fromAccountId, k -> new ArrayList<>()).add(transaction);
        transactionsByAccountId.computeIfAbsent(toAccountId, k -> new ArrayList<>()).add(transaction);
        logger.info(format("Transaction was created. From account {} to account {} with amount {}", fromAccountId, toAccountId, amount));
        return transaction;
    }

    public List<Transaction> getAll(long accountId) {
        return transactionsByAccountId.computeIfAbsent(accountId, k -> new ArrayList<>());
    }

    public BigDecimal getBalance(long accountId) {
        List<Transaction> transactions = getAll(accountId);
        BigDecimal plus = transactions.stream()
            .filter(transaction -> transaction.getTo() == accountId)
            .map(transaction -> transaction.getAmount())
            .reduce(new BigDecimal(0), (left, right) -> left.add(right));
        BigDecimal minus = transactions.stream()
            .filter(transaction -> transaction.getFrom() == accountId)
            .map(transaction -> transaction.getAmount())
            .reduce(new BigDecimal(0), (left, right) -> left.add(right));
        return plus.subtract(minus);
    }

}