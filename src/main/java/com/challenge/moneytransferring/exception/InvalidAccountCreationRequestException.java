package com.challenge.moneytransferring.exception;

public class InvalidAccountCreationRequestException extends RuntimeException {
  public InvalidAccountCreationRequestException(String message) {
    super(message);
  }
}