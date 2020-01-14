package com.challenge.moneytransferring.exception;

public class InvalidAccountBalanceException extends RuntimeException {
  public InvalidAccountBalanceException(String message) {
    super(message);
  }
}
