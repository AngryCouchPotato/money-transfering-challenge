package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private long from;
    private long to;
    private BigDecimal amount;
    private LocalDateTime localDateTime;

    public Transaction(long id, long from, long to, BigDecimal amount, LocalDateTime localDateTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.localDateTime = localDateTime;
    }

    public long getId() {
        return id;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

}
