package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.util.Jsons;
import spark.Route;

import static com.challenge.moneytransferring.util.Controllers.getAndValidatedId;

public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Route get() {
        return (request, response) -> accountService.get(getAndValidatedId(request));
    }

    public Route getAll() {
        return (request, response) -> accountService.getAll();
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
