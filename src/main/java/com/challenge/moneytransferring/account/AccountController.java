package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.exception.InvalidAccountCreationRequestException;
import com.challenge.moneytransferring.transaction.TransactionService;
import com.challenge.moneytransferring.util.Jsons;
import spark.Route;
import spark.utils.StringUtils;

import java.math.BigDecimal;

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

    public Route getBalance() {
        return (request, response) -> transactionService.getBalance(
            accountService.get(getAndValidatedId(request))
        );
    }

    public Route create() {
        return (request, response) -> {
            AccountCreationRequest acr = Jsons.fromJson(request.body(), AccountCreationRequest.class);
            validateRequest(acr);

            Account account = accountService.create(acr);
            response.status(201);
            return account;
        };
    }

    private void validateRequest(AccountCreationRequest request) {
        if (StringUtils.isBlank(request.getNumber())) {
            throw new InvalidAccountCreationRequestException("Account number can not be null or empty.");
        }

        if(request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAccountCreationRequestException("Initial amount of money has to be positive");
        }
    }
}
