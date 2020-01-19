package com.challenge.moneytransferring.integration;

import com.challenge.moneytransferring.MoneyTransferringApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

public abstract class IntegrationTest {

    @BeforeAll
    public static void setUp() throws InterruptedException {
        Thread.sleep(1000);// It is not the best solution, but I need this delay when I run all tests
        MoneyTransferringApplication.main(null);
    }

    @AfterAll
    public static void tearDown() {
        Spark.stop();
    }

    protected static TestResponse get(String path) {
        return request("GET", path, null);
    }

    protected static TestResponse post(String path, String json) {
        return request("POST", path, json);
    }

    protected static TestResponse request(String method, String path, String json) {
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
            if(connection.getResponseCode() == 400
                || connection.getResponseCode() == 404
                || connection.getResponseCode() == 422){
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

    public static class TestResponse {
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
