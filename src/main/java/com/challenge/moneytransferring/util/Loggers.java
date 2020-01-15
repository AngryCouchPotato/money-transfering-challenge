package com.challenge.moneytransferring.util;

import org.slf4j.helpers.MessageFormatter;

public class Loggers {
  public static String format(String format, Object... params) {
    return MessageFormatter.arrayFormat(format, params).getMessage();
  }
}
