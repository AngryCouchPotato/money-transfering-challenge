package com.challenge.moneytransferring.account;

import java.math.BigDecimal;

public class AccountCreationRequest {
    private String number;
    private BigDecimal amount;

    public AccountCreationRequest(String number, BigDecimal amount) {
        this.number = number;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

}
