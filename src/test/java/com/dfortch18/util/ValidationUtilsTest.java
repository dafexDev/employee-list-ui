package com.dfortch18.util;

import org.junit.jupiter.api.Test;

import com.dfortch18.util.ValidationUtils;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    public void testOnlyLetters() {
        assertTrue(ValidationUtils.onlyLetters("John"));
        assertFalse(ValidationUtils.onlyLetters("John123"));
        assertFalse(ValidationUtils.onlyLetters("John Doe"));
        assertFalse(ValidationUtils.onlyLetters(""));
        assertFalse(ValidationUtils.onlyLetters(" "));
    }

    @Test
    public void testEmail() {
        assertTrue(ValidationUtils.email("example@example.com"));
        assertTrue(ValidationUtils.email("user.name+tag+sorting@example.com"));
        assertFalse(ValidationUtils.email("plainaddress"));
        assertFalse(ValidationUtils.email("@missingusername.com"));
        assertFalse(ValidationUtils.email("username@.com"));
    }

    @Test
    public void testGreaterThanZeroInt() {
        assertTrue(ValidationUtils.greaterThanZero(1));
        assertFalse(ValidationUtils.greaterThanZero(0));
        assertFalse(ValidationUtils.greaterThanZero(-1));
    }

    @Test
    public void testGreaterThanZeroFloat() {
        assertTrue(ValidationUtils.greaterThanZero(1.0f));
        assertFalse(ValidationUtils.greaterThanZero(0.0f));
        assertFalse(ValidationUtils.greaterThanZero(-1.0f));
    }

    @Test
    public void testFloatParsable() {
        assertTrue(ValidationUtils.floatParsable("123.45"));
        assertTrue(ValidationUtils.floatParsable("0.0"));
        assertFalse(ValidationUtils.floatParsable("abc"));
        assertFalse(ValidationUtils.floatParsable("123.45abc"));
        assertFalse(ValidationUtils.floatParsable(""));
    }
}
