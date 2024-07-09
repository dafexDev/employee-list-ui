package com.dfortch18.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Logger log = LogManager.getLogger(ValidationUtils.class);

    private ValidationUtils() {
    }

    public static boolean onlyLetters(String text) {
        boolean result = !text.isEmpty() && Pattern.matches("[a-zA-Z]+", text);
        log.debug("Validation 'onlyLetters' for text '{}': {}", text, result);
        return result;
    }

    public static boolean email(String text) {
        boolean result = Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", text);
        log.debug("Validation 'email' for text '{}': {}", text, result);
        return result;
    }

    public static boolean greaterThanZero(int number) {
        boolean result = number > 0;
        log.debug("Validation 'greaterThanZero' for int number '{}': {}", number, result);
        return result;
    }

    public static boolean greaterThanZero(float number) {
        boolean result = number > 0;
        log.debug("Validation 'greaterThanZero' for float number '{}': {}", number, result);
        return result;
    }

    public static boolean floatParsable(String text) {
        try {
            Float.parseFloat(text);
            log.debug("Validation 'floatParsable' for text '{}': true", text);
            return true;
        } catch (NumberFormatException e) {
            log.debug("Validation 'floatParsable' for text '{}': false", text);
            return false;
        }
    }
}
