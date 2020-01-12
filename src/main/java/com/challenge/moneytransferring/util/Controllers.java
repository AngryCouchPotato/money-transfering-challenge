package com.challenge.moneytransferring.util;

import spark.Request;

public class Controllers {

    public static long getAndValidatedId(Request request) {
        String idString = request.params(":id");
        long id = validateAndGetNumber(idString);
        validateId(id);
        return id;
    }

    private static long validateAndGetNumber(String idString) {
        try{
            long id = Long.parseLong(idString);
            return id;
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException(String.format("Id should contains long value. Current value %s is incorrect", idString));
        }
    }

    private static void validateId(long id) {
        if(id <= 0) {
            throw new IllegalArgumentException(String.format("Id should be > 0. Current value %d is incorrect", id));
        }
    }
}
