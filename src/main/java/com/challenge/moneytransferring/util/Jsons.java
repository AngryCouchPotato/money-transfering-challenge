package com.challenge.moneytransferring.util;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class Jsons {

    public static <T> T fromJson(String body, Class<T> clazz) {
        return new Gson().fromJson(body, clazz);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return Jsons::toJson;
    }
}
