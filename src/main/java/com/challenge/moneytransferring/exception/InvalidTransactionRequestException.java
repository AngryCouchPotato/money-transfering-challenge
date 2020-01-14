package com.challenge.moneytransferring.exception;

public class InvalidTransactionRequestException extends RuntimeException {
  public InvalidTransactionRequestException(String message) {
    super(message);
  }
}
