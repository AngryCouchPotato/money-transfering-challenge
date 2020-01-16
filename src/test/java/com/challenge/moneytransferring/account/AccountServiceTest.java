package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.exception.EntityNotFountException;
import com.challenge.moneytransferring.transaction.Transaction;
import com.challenge.moneytransferring.transaction.TransactionStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;
    private TransactionStorage transactionStorage;

    @BeforeEach
    void setUp() {
        this.transactionStorage = new TransactionStorage();
        this.accountService = new AccountService(new AccountStorage(), transactionStorage);
    }

    @Test
    void shouldCreateAcountWithoutTransactions() {
        // Given
        String number = "Second";
        AccountCreationRequest request = new AccountCreationRequest(number, new BigDecimal(0));

        // When
        Account account = accountService.create(request);
        List<Transaction> transactions = transactionStorage.getAll(account.getId());

        // Then
        assertNotNull(account);
        assertEquals(2, account.getId());
        assertEquals(number, account.getNumber());
        assertNotNull(transactions);
        assertEquals(0, transactions.size());
    }

    @Test
    void shouldCreateAcountWithTransactions() {
        // Given
        String number = "Second";
        AccountCreationRequest request = new AccountCreationRequest(number, new BigDecimal(200));

        // When
        Account account = accountService.create(request);
        List<Transaction> transactions = transactionStorage.getAll(account.getId());

        // Then
        assertNotNull(account);
        assertEquals(2, account.getId());
        assertEquals(number, account.getNumber());
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    void shouldGetAccount() {
        // Given
        String number = "Second";
        AccountCreationRequest request = new AccountCreationRequest(number, new BigDecimal(0));
        long id = 2L;

        // When
        accountService.create(request);
        Account account = accountService.get(id);

        // Then
        assertNotNull(account);
        assertEquals(id, account.getId());
        assertEquals(number, account.getNumber());
    }

    @Test()
    void shouldFailGetAccount() {
        // Given
        long id = 777l;

        // When
        Throwable exception = assertThrows(EntityNotFountException.class, () -> accountService.get(id));

        // Then
        assertEquals("Could not find Entity with id = 777", exception.getMessage());
    }

    @Test
    void shouldGetAllAccounts() {
        // Given
        String number = "Second";
        AccountCreationRequest request = new AccountCreationRequest(number, new BigDecimal(0));

        // When
        accountService.create(request);
        List<Account> accounts = accountService.getAll();

        // Then
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertEquals("Base account", accounts.get(0).getNumber());
        assertEquals(number, accounts.get(1).getNumber());
    }
}