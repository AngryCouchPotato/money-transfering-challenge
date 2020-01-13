package com.challenge.moneytransferring;

import com.challenge.moneytransferring.account.AccountController;
import com.challenge.moneytransferring.account.AccountService;
import com.challenge.moneytransferring.account.AccountStorage;
import com.challenge.moneytransferring.transaction.TransactionController;
import com.challenge.moneytransferring.transaction.TransactionService;
import com.challenge.moneytransferring.transaction.TransactionStorage;
import com.challenge.moneytransferring.util.Path;

import static com.challenge.moneytransferring.util.Jsons.json;
import static spark.Spark.*;

public class MoneyTransferringApplication {

    private AccountController accountController;
    private TransactionController transactionController;

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
        AccountStorage accountStorage = new AccountStorage();
        TransactionService transactionService = new TransactionService(accountStorage, new TransactionStorage());
        
        this.accountController = new AccountController(new AccountService(accountStorage), transactionService);
        this.transactionController = new TransactionController(transactionService);

        get(Path.Web.ACCOUNTS,  accountController.getAll(), json());
        post(Path.Web.ACCOUNTS, accountController.create(), json());
        get(Path.Web.ACCOUNT_BY_ID, accountController.get(), json());
        get(Path.Web.TRANSACTIONS_OF_ACCOUNT, accountController.getAllTransactions(), json());
        delete(Path.Web.ACCOUNT_BY_ID, accountController.delete(), json());

        get(Path.Web.TRANSACTIONS, transactionController.getAll(), json());
        post(Path.Web.TRANSACTIONS, transactionController.create(), json());
        get(Path.Web.TRANSACTION_BY_ID, transactionController.get(), json());

    }

    public static void main(String[] args) {
        new MoneyTransferringApplication();

    }
}
