package com.challenge.moneytransferring.handler;

import com.challenge.moneytransferring.exception.EntityNotFountException;
import com.challenge.moneytransferring.exception.InvalidAccountBalanceException;
import com.challenge.moneytransferring.exception.InvalidTransactionRequestException;
import com.challenge.moneytransferring.util.Jsons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class.getName());

  public static spark.ExceptionHandler<? super java.lang.Exception> handle() {
    return (e, request, response) -> {
      logger.error(e.getMessage());
      response.type("application/json");
      response.status(getStatusCode(e));
      response.body(Jsons.toJson(new Error(e.getMessage())));
    };
  }

  private static int getStatusCode(Exception e) {
    int statusCode = 0;
    if (e instanceof EntityNotFountException) {
      statusCode = 404;
    } else if (e instanceof InvalidTransactionRequestException) {
      statusCode = 400;
    } else if (e instanceof InvalidAccountBalanceException) {
      statusCode = 422;
    } else {
      statusCode = 500;
    }
    return statusCode;
  }
}
