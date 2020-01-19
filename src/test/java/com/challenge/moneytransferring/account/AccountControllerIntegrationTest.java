package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.MoneyTransferringApplication;
import com.challenge.moneytransferring.transaction.Transaction;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.challenge.moneytransferring.util.Jsons.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerIntegrationTest {

    @BeforeAll
    public static void setUp() {
        MoneyTransferringApplication.main(null);
    }

    @AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    @Test
    @Order(1)
    void shouldCreateNewAccountWithoutTransaction() {
        // Given
        String number = "Second";
        BigDecimal amount = new BigDecimal(0);
        AccountCreationRequest request = new AccountCreationRequest(number, amount);

        // When
        TestResponse response = post("/accounts", toJson(request));

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
        TestResponse response = post("/accounts", toJson(request));

        // Then
        Account account = fromJson(response.body, Account.class);
        assertEquals(201, response.status);
        assertNotNull(account);
        assertEquals(number, account.getNumber());
        assertEquals(3, account.getId());
    }

    @Test
    @Order(3)
    void shouldGetAccount() {
        // Given
        String expectedNumber = "Second";
        long id = 2;

        // When
        TestResponse response = get("/accounts/" + id);

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
        TestResponse response = get("/accounts/" + id);

        // Then
        assertEquals(404, response.status);
        assertEquals("Not Found", response.message);
        assertNull(response.body);
    }

    @Test
    @Order(5)
    void shouldGetAllAccounts() {
        // When
        TestResponse response = get("/accounts");

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
        TestResponse response = get("/accounts/" + id + "/transactions");

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
        TestResponse response = get("/accounts/" + id + "/transactions");

        // Then
//        List<Transaction> transactions = objects(response.body);
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
        TestResponse response = get("/accounts/" + id + "/balance");

        // Then
        Double balance = object(response.body);
        assertEquals(200, response.status);
        assertNotNull(balance);
        assertEquals(expectedBalance, balance);
    }

    private TestResponse get(String path) {
        return request("GET", path, null);
    }

    private TestResponse post(String path, String json) {
        return request("POST", path, json);
    }

    private TestResponse request(String method, String path, String json) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            if(json != null){
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(json);
                osw.flush();
                osw.close();
                os.close();
            }

            connection.connect();
            if(connection.getResponseCode() == 404){
                return new TestResponse(connection.getResponseCode(), connection.getResponseMessage(), null);
            }
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), connection.getResponseMessage(), body);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    private static class TestResponse {

        public final String body;
        public final String message;
        public final int status;

        public TestResponse(int status, String message, String body) {
            this.status = status;
            this.message = message;
            this.body = body;
        }
    }
}