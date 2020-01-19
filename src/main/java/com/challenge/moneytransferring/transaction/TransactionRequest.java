package com.challenge.moneytransferring.transaction;

import java.math.BigDecimal;

public class TransactionRequest {

    private long from;
    private long to;
    private BigDecimal amount;

    public TransactionRequest(long from, long to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
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

    @Override
    public String toString() {
        return "TransactionRequest{" +
            "from=" + from +
            ", to=" + to +
            ", amount=" + amount +
            '}';
    }
}
