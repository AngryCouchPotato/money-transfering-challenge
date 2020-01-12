package com.challenge.moneytransferring.account;

import java.math.BigDecimal;

public class Account {

    private long id;
    private String number;
    private BigDecimal amount;

    public Account(long id, String number, BigDecimal amount) {
        this.id = id;
        this.number = number;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
