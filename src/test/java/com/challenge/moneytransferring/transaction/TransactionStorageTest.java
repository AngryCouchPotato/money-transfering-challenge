package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.Accounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionStorageTest {

    private TransactionStorage transactionStorage;

    @BeforeEach
    void setUp() {
        this.transactionStorage = new TransactionStorage();
    }

    @Test
    void shouldCreateTransaction() {
        // Given
        long second = 2L;
        long third = 3L;
        BigDecimal amount = new BigDecimal(50);

        // When
        Transaction transaction = transactionStorage.create(second, third, amount);

        // Then
        assertNotNull(transaction);
        assertEquals(1, transaction.getId());
        assertEquals(second, transaction.getFrom());
        assertEquals(third, transaction.getTo());
        assertTrue(amount.compareTo(transaction.getAmount()) == 0);
        assertNotNull(transaction.getLocalDateTime());
    }

    @Test
    void shouldGetAllEmptyList() {
        // Given
        long accountId = 1l;

        // When
        List<Transaction> transactions = transactionStorage.getAll(accountId);

        // Then
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void shouldGetAllFullList() {
        // Given
        long second = 2L;
        long third = 3L;

        // When
        transactionStorage.create(second, third, new BigDecimal(200));
        transactionStorage.create(third, second, new BigDecimal(100));
        transactionStorage.create(second, third, new BigDecimal(50));
        List<Transaction> transactionsSecond = transactionStorage.getAll(second);
        List<Transaction> transactionsThird = transactionStorage.getAll(third);

        // Then
        assertNotNull(transactionsSecond);
        assertNotNull(transactionsThird);
        assertFalse(transactionsSecond.isEmpty());
        assertFalse(transactionsThird.isEmpty());
        assertEquals(3, transactionsSecond.size());
        assertEquals(3, transactionsThird.size());
    }

    @Test
    void shouldGetBalance() {
        // Given
        long second = 2L;
        long third = 3L;

        // When
        transactionStorage.create(Accounts.BASE_ACCOUNT_ID, second, new BigDecimal(200));
        transactionStorage.create(Accounts.BASE_ACCOUNT_ID, third, new BigDecimal(200));
        transactionStorage.create(second, third, new BigDecimal(100));
        transactionStorage.create(third, second, new BigDecimal(50));
        transactionStorage.create(second, third, new BigDecimal(25));
        BigDecimal balanceSecond = transactionStorage.getBalance(second);
        BigDecimal balanceThird = transactionStorage.getBalance(third);

        // Then
        assertNotNull(balanceSecond);
        assertNotNull(balanceThird);
        assertEquals(275, balanceThird.intValue());
        assertEquals(125, balanceSecond.intValue());
    }
}