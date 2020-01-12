package com.challenge.moneytransferring.exception;

public class EntityNotFountException extends RuntimeException {
    private long id;

    public EntityNotFountException(String message, long id) {
        super(message);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
