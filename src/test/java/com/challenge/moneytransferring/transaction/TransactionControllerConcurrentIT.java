package com.challenge.moneytransferring.transaction;

import com.challenge.moneytransferring.account.AccountCreationRequest;
import com.challenge.moneytransferring.integration.IntegrationTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.challenge.moneytransferring.util.Jsons.object;
import static com.challenge.moneytransferring.util.Jsons.toJson;
import static com.challenge.moneytransferring.util.Path.Web.ACCOUNTS;
import static com.challenge.moneytransferring.util.Path.Web.TRANSACTIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionControllerConcurrentIT extends IntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(TransactionControllerConcurrentIT.class.getName());

    @BeforeAll
    public static void init() {
      post(ACCOUNTS, toJson(new AccountCreationRequest("Second", new BigDecimal(200))));
      post(ACCOUNTS, toJson(new AccountCreationRequest("Third", new BigDecimal(300))));
      post(ACCOUNTS, toJson(new AccountCreationRequest("Fourt", new BigDecimal(400))));
      post(ACCOUNTS, toJson(new AccountCreationRequest("Fifth", new BigDecimal(500))));
      post(ACCOUNTS, toJson(new AccountCreationRequest("Sixth", new BigDecimal(600))));
    }

    @Test
    @Order(1)
    public void shouldDoTransactions() {
        int min = 2;
        int max = 3;
        int totalNumberOfTasks = max - 1;
        CountDownLatch latch = new CountDownLatch(totalNumberOfTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalNumberOfTasks);
        AtomicInteger requestCounter = new AtomicInteger();
        for(int i = min; i <= max; i++) {
            TransactionRequest request;
            if(i == max) {
              request = new TransactionRequest(i, min, new BigDecimal(2));
            } else {
              request = new TransactionRequest(i, i + 1, new BigDecimal(2));
            }
            executorService.execute(() -> {
              for(int j = 0; j < 100; j++) {
                  post(TRANSACTIONS, toJson(request));
                  requestCounter.incrementAndGet();
                  logger.info("accountId = {}, iteration = {} {}", request.getFrom(), j, request);
                  if(j > 0 && j % 2 == 0) {
                      TestResponse responseFrom = get(ACCOUNTS + "/" + request.getFrom() + "/balance");
                      Double balanceFrom = object(responseFrom.body);
                      logger.info("account = {} has balance = {}", request.getFrom(), balanceFrom);

                      TestResponse responseTo = get(ACCOUNTS + "/" + request.getTo() + "/balance");
                      Double balanceTo = object(responseTo.body);
                      logger.info("account = {} has balance = {}", request.getFrom(), balanceTo);
                  }
              }
              latch.countDown();
          });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        logger.info("requestCounter = {}", requestCounter.get());

        for(long i = min; i <= max; i++) {
            Map<Long, Double> balances = new HashMap<>();
            balances.put(2L, 200D);
            balances.put(3L, 300D);

            // When
            TestResponse response = get(ACCOUNTS + "/" + i + "/balance");

            // Then
            Double balance = object(response.body);
            logger.info("Account with id = {} has balance = {}", i, balance);
            assertEquals(200, response.status);
            assertNotNull(balance);
            assertEquals(balances.get(i), balance);
        }
    }

    @Test
    @Order(2)
    public void shouldToTransactionsWithBalanceRotation() {
        int min = 2;
        int max = 6;
        int totalNumberOfTasks = max - 1;
        CountDownLatch latch = new CountDownLatch(totalNumberOfTasks);
        ExecutorService executorService = Executors.newFixedThreadPool(totalNumberOfTasks);
        AtomicInteger requestCounter = new AtomicInteger();
        for(int i = min; i <= max; i++) {
            TransactionRequest request;
            if(i == max) {
                request = new TransactionRequest(i, min, new BigDecimal(i));
            } else {
                request = new TransactionRequest(i, i + 1, new BigDecimal(i));
            }
            executorService.execute(() -> {
                for(int j = 0; j < 100; j++) {
                    post(TRANSACTIONS, toJson(request));
                    requestCounter.incrementAndGet();
                    logger.info("accountId = {}, iteration = {} {}", request.getFrom(), j, request);
                    if(j > 0 && j % 10 == 0) {
                        TestResponse responseFrom = get(ACCOUNTS + "/" + request.getFrom() + "/balance");
                        Double balanceFrom = object(responseFrom.body);
                        logger.info("account = {} has balance = {}", request.getFrom(), balanceFrom);

                        TestResponse responseTo = get(ACCOUNTS + "/" + request.getTo() + "/balance");
                        Double balanceTo = object(responseTo.body);
                        logger.info("account = {} has balance = {}", request.getFrom(), balanceTo);
                    }
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        logger.info("requestCounter = {}", requestCounter.get());

        for(long i = min; i <= max; i++) {
            Map<Long, Double> balances = new HashMap<>();
            balances.put(2L, 600D);
            balances.put(3L, 200D);
            balances.put(4L, 300D);
            balances.put(5L, 400D);
            balances.put(6L, 500D);

            // When
            TestResponse response = get(ACCOUNTS + "/" + i + "/balance");

            // Then
            Double balance = object(response.body);
            logger.info("Account with id = {} has balance = {}", i, balance);
            assertEquals(200, response.status);
            assertNotNull(balance);
            assertEquals(balances.get(i), balance);
        }
    }
}
