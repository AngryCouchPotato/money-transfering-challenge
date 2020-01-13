package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.transaction.TransactionService;
import com.challenge.moneytransferring.util.Jsons;
import spark.Route;

import static com.challenge.moneytransferring.util.Controllers.getAndValidatedId;

public class AccountController {

    private AccountService accountService;
    private TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public Route get() {
        return (request, response) -> accountService.get(getAndValidatedId(request));
    }

    public Route getAll() {
        return (request, response) -> accountService.getAll();
    }

    public Route getAllTransactions() {
        return (request, response) -> transactionService.getAll(getAndValidatedId(request));
    }

    public Route create() {
        return (request, response) -> {
            CreateAccountRequest acr = Jsons.fromJson(request.body(), CreateAccountRequest.class);
            // todo validate

            Account account = accountService.create(acr);
            response.status(201);
            return account;
        };
    }

    public Route delete() {
        return (request, response) -> accountService.delete(getAndValidatedId(request));
    }
}
