package com.dafortch;

import com.dafortch.io.CsvEmployeesLoader;
import com.dafortch.io.JsonEmployeesLoader;
import com.dafortch.model.Employee;
import com.dafortch.props.ApplicationProperties;
import com.dafortch.props.ResourceBasedApplicationProperties;
import com.dafortch.repository.EmployeeRepository;
import com.dafortch.repository.Repository;
import com.dafortch.ui.EmployeeListUI;
import com.dafortch.util.DatabaseConnection;
import com.dafortch.validation.DefaultEmployeeValidator;
import com.dafortch.validation.EmployeeValidator;
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