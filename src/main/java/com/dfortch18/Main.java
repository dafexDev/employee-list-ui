package com.dfortch18;

import com.dfortch18.io.CsvEmployeesLoader;
import com.dfortch18.io.JsonEmployeesLoader;
import com.dfortch18.model.Employee;
import com.dfortch18.props.ApplicationProperties;
import com.dfortch18.props.ResourceBasedApplicationProperties;
import com.dfortch18.repository.EmployeeRepository;
import com.dfortch18.repository.Repository;
import com.dfortch18.ui.EmployeeListUI;
import com.dfortch18.util.DatabaseConnection;
import com.dfortch18.validation.DefaultEmployeeValidator;
import com.dfortch18.validation.EmployeeValidator;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.sql.Connection;
import java.util.Locale;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH); // Set all locale to EN
        log.info("Locale set to English");

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            log.info("Look and feel set to FlatLightLaf");
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Failed to set look and feel", e);
        }

        SwingUtilities.invokeLater(() -> {
            try {
                log.trace("Starting application initialization");

                ApplicationProperties properties = new ResourceBasedApplicationProperties(ApplicationConstants.APPLICATION_PROPERTIES_PATH);
                log.debug("Application properties loaded from {}", ApplicationConstants.APPLICATION_PROPERTIES_PATH);

                EmployeeValidator employeeValidator = new DefaultEmployeeValidator();
                log.debug("Employee validator initialized");

                CsvEmployeesLoader csvEmployeesLoader = new CsvEmployeesLoader(employeeValidator);
                JsonEmployeesLoader jsonEmployeesLoader = new JsonEmployeesLoader(employeeValidator);

                log.debug("CSV and JSON employees loaders initialized");

                Connection connection = DatabaseConnection.getConnection(properties.jdbcUrl(), properties.jdbcUser(),
                        properties.jdbcPassword(), properties.jdbcDriver());
                log.debug("Database connection obtained");

                Repository<Employee> employeeRepository = new EmployeeRepository(connection);
                log.debug("Employee repository initialized");

                EmployeeListUI ui = new EmployeeListUI(employeeRepository, employeeValidator, csvEmployeesLoader, jsonEmployeesLoader);
                ui.setVisible(true);
                log.info("EmployeeListUI set visible");
            } catch (Exception e) {
                log.error("Failed to initialize application", e);
                JOptionPane.showMessageDialog(null,
                        "Failed to initialize application:\n\n" + e,
                        "Application failed to start",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}