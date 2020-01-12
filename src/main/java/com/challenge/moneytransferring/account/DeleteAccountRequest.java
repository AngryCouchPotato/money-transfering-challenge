package com.challenge.moneytransferring.account;

public class DeleteAccountRequest {
    private String number;

    public DeleteAccountRequest() {
    }

    public DeleteAccountRequest(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
