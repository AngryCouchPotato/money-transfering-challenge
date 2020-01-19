  package com.challenge.moneytransferring.transaction;

  import com.challenge.moneytransferring.IntegrationTest;
  import com.challenge.moneytransferring.account.AccountCreationRequest;
  import com.google.gson.reflect.TypeToken;
  import org.junit.jupiter.api.*;

  import java.math.BigDecimal;
  import java.util.List;

  import static com.challenge.moneytransferring.util.Jsons.fromJson;
  import static com.challenge.moneytransferring.util.Jsons.toJson;
  import static com.challenge.moneytransferring.util.Path.Web.ACCOUNTS;
  import static com.challenge.moneytransferring.util.Path.Web.TRANSACTIONS;
  import static org.junit.jupiter.api.Assertions.*;

  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  class TransactionControllerIntegrationTest extends IntegrationTest {

      @BeforeAll
      private static void init() {
          post(ACCOUNTS, toJson(new AccountCreationRequest("Second", new BigDecimal(200))));
          post(ACCOUNTS, toJson(new AccountCreationRequest("Third", new BigDecimal(200))));
      }

      @Test
      @Order(1)
      void shouldCreateNewTransaction() {
          // Given
          long fromId = 2;
          long toId = 3;
          BigDecimal amount = new BigDecimal(100);
          TransactionRequest request = new TransactionRequest(fromId, toId, amount);

          // When
          TestResponse response = post(TRANSACTIONS, toJson(request));

          // Then
          Transaction transaction = fromJson(response.body, Transaction.class);
          assertEquals(201, response.status);
          assertNotNull(transaction);
          assertEquals(3, transaction.getId());
          assertEquals(fromId, transaction.getFrom());
          assertEquals(toId, transaction.getTo());
          assertEquals(amount, transaction.getAmount());
          assertNotNull(transaction.getLocalDateTime());
      }

      @Test
      @Order(2)
      void shouldFailCreateNewTransaction() {
          // Given
          long fromId = 2;
          long toId = 3;
          BigDecimal amount = new BigDecimal(999);
          TransactionRequest request = new TransactionRequest(fromId, toId, amount);

          // When
          TestResponse response = post(TRANSACTIONS, toJson(request));

          // Then
          assertEquals(422, response.status);
          assertEquals("Unprocessable Entity", response.message);
          assertNull(response.body);
      }

      @Test
      @Order(2)
      void shouldFailCreateNewTransactionWithNegativeFromAccountId() {
          // Given
          long fromId = -2;
          long toId = 3;
          BigDecimal amount = new BigDecimal(999);
          TransactionRequest request = new TransactionRequest(fromId, toId, amount);

          // When
          TestResponse response = post(TRANSACTIONS, toJson(request));

          // Then
          assertEquals(400, response.status);
          assertEquals("Bad Request", response.message);
          assertNull(response.body);
      }

      @Test
      @Order(2)
      void shouldFailCreateNewTransactionWithEqualsAccounts() {
          // Given
          long fromId = 2;
          long toId = 2;
          BigDecimal amount = new BigDecimal(999);
          TransactionRequest request = new TransactionRequest(fromId, toId, amount);

          // When
          TestResponse response = post(TRANSACTIONS, toJson(request));

          // Then
          assertEquals(400, response.status);
          assertEquals("Bad Request", response.message);
          assertNull(response.body);
      }

      @Test
      @Order(2)
      void shouldFailCreateNewTransactionWithNegativeAmount() {
          // Given
          long fromId = 2;
          long toId = 3;
          BigDecimal amount = new BigDecimal(-222);
          TransactionRequest request = new TransactionRequest(fromId, toId, amount);

          // When
          TestResponse response = post(TRANSACTIONS, toJson(request));

          // Then
          assertEquals(400, response.status);
          assertEquals("Bad Request", response.message);
          assertNull(response.body);
      }

      @Test
      @Order(3)
      void shouldGetTransaction() {
          // Given
          BigDecimal expectedAmount = new BigDecimal(200);
          long id = 1;

          // When
          TestResponse response = get(TRANSACTIONS + "/" + id);

          // Then
          Transaction transaction = fromJson(response.body, Transaction.class);
          assertEquals(200, response.status);
          assertEquals(id, transaction.getId());
          assertEquals(1L, transaction.getFrom());
          assertEquals(2L, transaction.getTo());
          assertEquals(expectedAmount, transaction.getAmount());
          assertNotNull(transaction.getLocalDateTime());
      }

      @Test
      @Order(4)
      void shouldGetAllTransactions() {
          // When
          TestResponse response = get(TRANSACTIONS);

          // Then
          List<Transaction> transactions = fromJson(response.body, new TypeToken<List<Transaction>>(){}.getType());
          assertEquals(200, response.status);
          assertNotNull(transactions);
          assertEquals(3, transactions.size());
      }
  }