package com.challenge.moneytransferring.transaction;

import java.math.BigDecimal;

public class TransactionRequest {

    private long from;
    private long to;
    private BigDecimal amount;

    public TransactionRequest() {
    }

    public TransactionRequest(long from, long to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
