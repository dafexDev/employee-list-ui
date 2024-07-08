package com.dafex.props;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dafortch.props.ResourceBasedApplicationProperties;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceBasedApplicationPropertiesTest {

    private ResourceBasedApplicationProperties properties;

    @BeforeEach
    public void setUp() throws IOException {
        properties = new ResourceBasedApplicationProperties("/test-config.properties");
    }

    @Test
    public void testJdbcUrl() {
        assertEquals("jdbc:mysql://localhost:3306/mydb", properties.jdbcUrl());
    }

    @Test
    public void testJdbcUser() {
        assertEquals("root", properties.jdbcUser());
    }

    @Test
    public void testJdbcPassword() {
        assertEquals("password", properties.jdbcPassword());
    }

    @Test
    public void testJdbcDriver() {
        assertEquals("com.mysql.cj.jdbc.Driver", properties.jdbcDriver());
    }

    @Test
    public void testFileNotFound() {
        Exception exception = assertThrows(IOException.class, () -> {
            new ResourceBasedApplicationProperties("/non-existent-config.properties");
        });

        String expectedMessage = "File not found: /non-existent-config.properties";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
