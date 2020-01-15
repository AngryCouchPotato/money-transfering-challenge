package com.challenge.moneytransferring.account;

import java.math.BigDecimal;

public class AccountCreationRequest {
    private String number;
    private BigDecimal amount;

    public AccountCreationRequest() {
    }

    public AccountCreationRequest(String number, BigDecimal amount) {
        this.number = number;
        this.amount = amount;
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
