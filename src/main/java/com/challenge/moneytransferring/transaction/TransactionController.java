package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.util.Jsons;
import spark.Route;

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
            // todo validate

            Transaction transaction = transactionService.create(transactionRequest);
            response.status(201);
            return transaction;
        };
    }
}
