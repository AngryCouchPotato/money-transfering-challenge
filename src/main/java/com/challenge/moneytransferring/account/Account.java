package com.challenge.moneytransferring.account;

public class Account {

    private long id;
    private String number;

    public Account(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

}
