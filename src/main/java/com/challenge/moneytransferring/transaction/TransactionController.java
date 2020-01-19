package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.exception.InvalidTransactionRequestException;
import com.challenge.moneytransferring.util.Jsons;
import spark.Route;

import java.math.BigDecimal;

import static com.challenge.moneytransferring.util.Controllers.getAndValidatedId;

public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public Route get() {
        return (request, response) -> transactionService.get(getAndValidatedId(request));
    }

    public Route getAll() {
        return (request, response) -> transactionService.getAll();
    }

    public Route create() {
        return (request, response) -> {
            TransactionRequest transactionRequest = Jsons.fromJson(request.body(), TransactionRequest.class);
            validateRequest(transactionRequest);

            Transaction transaction = transactionService.create(transactionRequest);
            response.status(201);
            return transaction;
        };
    }

    private void validateRequest(TransactionRequest request) {
        if (request.getFrom() <= 0
            || request.getTo() <= 0) {
            throw new InvalidTransactionRequestException("AccountId can not be less o equal zero");
        }
        if(request.getFrom() == request.getTo()) {
            throw new InvalidTransactionRequestException(String.format("Accounts have to be different. base account = %d, destination account = %d", request.getFrom(), request.getTo()));
        }
        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionRequestException(String.format("Amount (%s) cannot be less or equals 0.", request.getAmount()));
        }
    }
}
