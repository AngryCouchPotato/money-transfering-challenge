package com.challenge.moneytransferring.util;

public class Path {
    public static class Web {
        public static final String ACCOUNTS = "/accounts";
        public static final String ACCOUNT_BY_ID = "/accounts/:id";
        public static final String ACCOUNT_BALANCCE = "/accounts/:id/balance";
        public static final String TRANSACTIONS_OF_ACCOUNT = "/accounts/:id/transactions";
        public static final String TRANSACTIONS = "/transactions";
        public static final String TRANSACTION_BY_ID = "/transactions/:id";
    }
}
