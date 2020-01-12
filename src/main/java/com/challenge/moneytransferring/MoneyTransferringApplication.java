package com.challenge.moneytransferring;

import com.challenge.moneytransferring.account.AccountController;
import com.challenge.moneytransferring.account.AccountService;
import com.challenge.moneytransferring.account.AccountStorage;
import com.challenge.moneytransferring.util.Path;

import static com.challenge.moneytransferring.util.Jsons.json;
import static spark.Spark.*;

public class MoneyTransferringApplication {

    private AccountController accountController;

    public MoneyTransferringApplication() {
        initSpark();
        init();
    }

    private void initSpark() {
        // Configure Spark
        port(4567);
//        staticFiles.expireTime(600L);
    }

    private void init() {
        this.accountController = new AccountController(new AccountService(new AccountStorage()));

        get(Path.Web.ACCOUNTS,  accountController.getAll(), json());
        post(Path.Web.ACCOUNTS, accountController.create(), json());
        get(Path.Web.ACCOUNT_BY_ID, accountController.get(), json());
        delete(Path.Web.ACCOUNT_BY_ID, accountController.delete(), json());
//        post(Path.Web.TRANSACTIONS, ); todo
    }

    public static void main(String[] args) {
        new MoneyTransferringApplication();

    }
}
