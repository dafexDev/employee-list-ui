package com.dfortch18.props;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceBasedApplicationProperties implements ApplicationProperties {

    private static final Logger log = LogManager.getLogger(ResourceBasedApplicationProperties.class);

    private final Properties properties = new Properties();

    public ResourceBasedApplicationProperties(String propertiesFilePath) throws IOException {
        log.trace("Initializing ResourceBasedApplicationProperties with file: {}", propertiesFilePath);

        try (InputStream inputStream = getClass().getResourceAsStream(propertiesFilePath)) {
            if (inputStream == null) {
                log.error("Properties file not found");
                throw new IOException("File not found: " + propertiesFilePath);
            }
            log.debug("Properties file found, loading properties...");
            properties.load(inputStream);
            log.info("Properties loaded successfully from file: {}", propertiesFilePath);
        } catch (Exception e) {
            log.error("Failed to load properties from file: {}", propertiesFilePath, e);
            throw e;
        }
    }

    @Override
    public String jdbcUrl() {
        String value = properties.getProperty("jdbc.url");
        log.trace("Fetching jdbc.url: {}", value);
        return value;
    }

    @Override
    public String jdbcUser() {
        String value = properties.getProperty("jdbc.user");
        log.trace("Fetching jdbc.user: {}", "[PROTECTED]");
        return value;
    }

    @Override
    public String jdbcPassword() {
        String value = properties.getProperty("jdbc.password");
        log.trace("Fetching jdbc.password: {}", "[PROTECTED]");
        return value;
    }

    @Override
    public String jdbcDriver() {
        String value = properties.getProperty("jdbc.driver");
        log.trace("Fetching jdbc.driver: {}", value);
        return value;
    }
}
