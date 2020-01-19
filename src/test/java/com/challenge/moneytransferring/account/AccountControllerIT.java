package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.integration.IntegrationTest;
import com.challenge.moneytransferring.transaction.Transaction;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.util.List;

import static com.challenge.moneytransferring.util.Jsons.*;
import static com.challenge.moneytransferring.util.Path.Web.ACCOUNTS;
import static com.challenge.moneytransferring.util.Path.Web.TRANSACTIONS;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIT extends IntegrationTest {

    @Test
    @Order(1)
    void shouldCreateNewAccountWithoutTransaction() {
        // Given
        String number = "Second";
        BigDecimal amount = new BigDecimal(0);
        AccountCreationRequest request = new AccountCreationRequest(number, amount);

        // When
        TestResponse response = post(ACCOUNTS, toJson(request));

        // Then
        Account account = fromJson(response.body, Account.class);
        assertEquals(201, response.status);
        assertNotNull(account);
        assertEquals(number, account.getNumber());
        assertEquals(2, account.getId());
    }

    @Test
    @Order(2)
    void shouldCreateNewAccountWithTransaction() {
        // Given
        String number = "Third";
        BigDecimal amount = new BigDecimal(100);
        AccountCreationRequest request = new AccountCreationRequest(number, amount);

        // When
        TestResponse response = post(ACCOUNTS, toJson(request));

        // Then
        Account account = fromJson(response.body, Account.class);
        assertEquals(201, response.status);
        assertNotNull(account);
        assertEquals(number, account.getNumber());
        assertEquals(3, account.getId());
    }

    @Test
    @Order(2)
    void shouldFailCreateNewAccountWithBlankAccountNumber() {
        // Given
        String number = "";
        BigDecimal amount = new BigDecimal(100);
        AccountCreationRequest request = new AccountCreationRequest(number, amount);

        // When
        TestResponse response = post(ACCOUNTS, toJson(request));

        // Then
        assertEquals(400, response.status);
        assertEquals("Bad Request", response.message);
        assertNull(response.body);
    }

    @Test
    @Order(2)
    void shouldFailCreateNewAccountWithNegativeAmount() {
        // Given
        String number = "Fourth";
        BigDecimal amount = new BigDecimal(-100);
        AccountCreationRequest request = new AccountCreationRequest(number, amount);

        // When
        TestResponse response = post(ACCOUNTS, toJson(request));

        // Then
        assertEquals(400, response.status);
        assertEquals("Bad Request", response.message);
        assertNull(response.body);
    }


    @Test
    @Order(3)
    void shouldGetAccount() {
        // Given
        String expectedNumber = "Second";
        long id = 2;

        // When
        TestResponse response = get(ACCOUNTS + "/" + id);

        // Then
        Account account = fromJson(response.body, Account.class);
        assertEquals(200, response.status);
        assertEquals(expectedNumber, account.getNumber());
        assertEquals(id, account.getId());
    }

    @Test
    @Order(4)
    void shouldFailGetAccount() {
        // Given
        long id = 777L;

        // When
        TestResponse response = get(ACCOUNTS + "/" + id);

        // Then
        assertEquals(404, response.status);
        assertEquals("Not Found", response.message);
        assertNull(response.body);
    }

    @Test
    @Order(5)
    void shouldGetAllAccounts() {
        // When
        TestResponse response = get(ACCOUNTS);

        // Then
        List<Account> accounts = fromJson(response.body, new TypeToken<List<Account>>(){}.getType());
        assertEquals(200, response.status);
        assertNotNull(accounts);
        assertEquals(3, accounts.size());
    }

    @Test
    @Order(6)
    void shouldFailGetAllTransactionsByAccountId() {
        // Given
        long id = 2;

        // When
        TestResponse response = get(ACCOUNTS + "/" + id + TRANSACTIONS);

        // Then
        List<Transaction> transactions = fromJson(response.body, new TypeToken<List<Transaction>>(){}.getType());
        assertEquals(200, response.status);
        assertTrue(transactions.isEmpty());
    }

    @Test
    @Order(7)
    void shouldGetAllTransactionsByAccountId() {
        // Given
        long id = 3;

        // When
        TestResponse response = get(ACCOUNTS + "/" + id + TRANSACTIONS);

        // Then
        List<Transaction> transactions = fromJson(response.body, new TypeToken<List<Transaction>>(){}.getType());
        assertEquals(200, response.status);
        assertNotNull(transactions);
        assertFalse(transactions.isEmpty());
    }

    @Test
    @Order(8)
    void shouldGetAccountBalance() {
        // Given
        long id = 3;
        double expectedBalance = 100;

        // When
        TestResponse response = get(ACCOUNTS + "/" + id + "/balance");

        // Then
        Double balance = object(response.body);
        assertEquals(200, response.status);
        assertNotNull(balance);
        assertEquals(expectedBalance, balance);
    }
}