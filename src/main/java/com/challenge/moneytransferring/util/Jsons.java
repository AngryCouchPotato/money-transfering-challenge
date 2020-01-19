package com.challenge.moneytransferring.util;

import com.challenge.moneytransferring.account.Account;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.ResponseTransformer;

import java.lang.reflect.Type;

public class Jsons {

    public static <T> T fromJson(String body, Class<T> clazz) {
        return new Gson().fromJson(body, clazz);
    }

    public static <T> T fromJson(String body, Type type) {
        return new Gson().fromJson(body, type);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T object(String body) {
        return fromJson(body, new TypeToken<T>(){}.getType());
    }

    public static Account account(String body) {
        return fromJson(body, Account.class);
    }

    public static ResponseTransformer json() {
        return Jsons::toJson;
    }
}
