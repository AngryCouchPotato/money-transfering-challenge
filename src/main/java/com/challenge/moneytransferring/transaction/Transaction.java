package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private Account from;
    private Account to;
    private BigDecimal amount;
    private LocalDateTime localDateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
