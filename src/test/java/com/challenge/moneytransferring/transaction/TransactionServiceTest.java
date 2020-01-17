package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.*;
import com.challenge.moneytransferring.exception.EntityNotFountException;
import com.challenge.moneytransferring.exception.InvalidAccountBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

  private TransactionService transactionService;
  private TransactionStorage transactionStorage;
  private AccountService accountService;
  private AccountStorage accountStorage;

  @BeforeEach
  void setUp() {
      this.transactionStorage = new TransactionStorage();
      this.accountStorage = new AccountStorage();
      this.transactionService = new TransactionService(accountStorage, transactionStorage);
      this.accountService = new AccountService(accountStorage, transactionStorage);
  }

  @Test
  void shouldCreateTransaction() {
      // Given
      long secondId = 2L;
      long thirdId = 3L;
      BigDecimal amount = new BigDecimal(50);
      TransactionRequest transactionRequest = new TransactionRequest(secondId, thirdId, amount);
      accountService.create(new AccountCreationRequest("Second", new BigDecimal(200)));
      accountService.create(new AccountCreationRequest("Third", new BigDecimal(300)));

      // When
      Transaction transaction = transactionService.create(transactionRequest);

      // Then
      assertNotNull(transaction);
      assertEquals(3, transaction.getId());
      assertEquals(secondId, transaction.getFrom());
      assertEquals(thirdId, transaction.getTo());
      assertTrue(amount.compareTo(transaction.getAmount()) == 0);
      assertNotNull(transaction.getLocalDateTime());
  }

  @Test
  void shouldGetTransaction() {
      // Given
      long second = 2L;
      long third = 3L;
      BigDecimal amount = new BigDecimal(50);
      long id = 1L;
      transactionStorage.create(second, third, amount);

      // When
      Transaction transaction = transactionService.get(id);

      // Then
      assertNotNull(transaction);
      assertEquals(1L, transaction.getId());
      assertEquals(second, transaction.getFrom());
      assertEquals(third, transaction.getTo());
      assertEquals(amount, transaction.getAmount());
      assertNotNull(transaction.getLocalDateTime());
  }

  @Test()
  void shouldFailGetAccount() {
      // Given
      long id = 777L;

      // When
      Throwable exception = assertThrows(EntityNotFountException.class, () -> transactionService.get(id));

      // Then
      assertEquals("Could not find Entity with id = 777", exception.getMessage());
  }

  @Test
  void shouldGetAllByAccountIdEmptyList() {
      // Given
      long accountId = 1l;

      // When
      List<Transaction> transactions = transactionService.getAll(accountId);

      // Then
      assertNotNull(transactions);
      assertTrue(transactions.isEmpty());
    }

  @Test
  void shouldGetAllByAccountIdFullList() {
      // Given
      long second = 2L;
      long third = 3L;
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, third, new BigDecimal(200));
      transactionStorage.create(third, second, new BigDecimal(100));
      transactionStorage.create(second, third, new BigDecimal(50));

      // When
      List<Transaction> transactionsSecond = transactionService.getAll(second);
      List<Transaction> transactionsThird = transactionService.getAll(third);

      // Then
      assertNotNull(transactionsSecond);
      assertNotNull(transactionsThird);
      assertFalse(transactionsSecond.isEmpty());
      assertFalse(transactionsThird.isEmpty());
      assertEquals(2, transactionsSecond.size());
      assertEquals(3, transactionsThird.size());
  }

  @Test
  void shouldGetAllFullList() {
      // Given
      long second = 2L;
      long third = 3L;
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, third, new BigDecimal(200));
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, second, new BigDecimal(100));
      transactionStorage.create(second, third, new BigDecimal(50));

      // When
      List<Transaction> transactions = transactionService.getAll();

      // Then
      assertNotNull(transactions);
      assertFalse(transactions.isEmpty());
      assertEquals(3, transactions.size());
    }

  @Test
  void shouldGetAllEmptyList() {
      // When
      List<Transaction> transactions = transactionService.getAll();

      // Then
      assertNotNull(transactions);
      assertTrue(transactions.isEmpty());
  }

  @Test
  void shouldGetBalance() {
      // Given
      long second = 2L;
      long third = 3L;
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, second, new BigDecimal(200));
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, third, new BigDecimal(200));
      transactionStorage.create(second, third, new BigDecimal(100));
      transactionStorage.create(third, second, new BigDecimal(50));
      transactionStorage.create(second, third, new BigDecimal(25));

      // When
      BigDecimal balanceSecond = transactionService.getBalance(new Account(second, "Second"));
      BigDecimal balanceThird = transactionService.getBalance(new Account(third, "Third"));

      // Then
      assertNotNull(balanceSecond);
      assertNotNull(balanceThird);
      assertEquals(275, balanceThird.intValue());
      assertEquals(125, balanceSecond.intValue());
  }

  @Test
  void shouldFailValidateAccount() {
      // Given
      long second = 2L;
      long third = 3L;
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, second, new BigDecimal(200));
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, third, new BigDecimal(200));
      transactionStorage.create(second, third, new BigDecimal(100));
      transactionStorage.create(third, second, new BigDecimal(50));
      transactionStorage.create(second, third, new BigDecimal(25));

      // When
      Throwable exception = assertThrows(InvalidAccountBalanceException.class, () ->
          transactionService.validateAccount(new Account(second, "Second"), new BigDecimal(200))
      );

      // Then
      assertEquals("Account with id = 2 and number = Second doesn't contain enough money", exception.getMessage());
  }

  @Test
  void shouldPassValidateAccount() {
      // Given
      long second = 2L;
      long third = 3L;
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, second, new BigDecimal(200));
      transactionStorage.create(Accounts.BASE_ACCOUNT_ID, third, new BigDecimal(200));
      transactionStorage.create(second, third, new BigDecimal(100));
      transactionStorage.create(third, second, new BigDecimal(50));
      transactionStorage.create(second, third, new BigDecimal(25));


      // When
      transactionService.validateAccount(new Account(third, "Second"), new BigDecimal(200));
  }
}