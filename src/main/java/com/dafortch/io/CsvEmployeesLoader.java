package com.dafortch.io;

import com.dafortch.model.Employee;
import com.dafortch.util.BufferReadUtils;
import com.dafortch.validation.EmployeeValidator;
import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CsvEmployeesLoader extends AbstractEmployeesLoader {

    private static final Logger log = LogManager.getLogger(CsvEmployeesLoader.class);

    public CsvEmployeesLoader(EmployeeValidator employeeValidator) {
        super(employeeValidator);
    }

    @Override
    public List<Employee> loadEmployees(String csvFilePath) throws Exception {
        List<Employee> employees = new ArrayList<>();
        log.trace("Starting to load CSV file: {}", csvFilePath);

        try (CSVReader csvReader = new CSVReader(BufferReadUtils.removeBOMFromFile(csvFilePath))) {
            String[] values;

            while ((values = csvReader.readNext()) != null) {
                log.trace("Reading row: {}", (Object) values);

                if (values.length == 4) {
                    String firstName = values[0].trim();
                    String surName = values[1].trim();
                    String email = values[2].trim();
                    String salaryText = values[3].trim();

                    try {
                        employeeValidator.validateEmployeeValues(firstName, surName, email, salaryText);
                        log.debug("Validated employee values: firstName={} surName={}, email={}, salaryText={}", firstName, surName, email, salaryText);

                        Float salary = Float.parseFloat(salaryText);

                        Employee employee = new Employee(firstName, surName, email, salary);
                        employees.add(employee);
                    } catch (Exception e) {
                        log.warn("Validation failed or parsing error for row: {}", values, e);
                        throw e;
                    }
                } else {
                    log.warn("Invalid row length: {}", (Object) values);
                }
            }
        } catch (Exception e) {
            log.error("Error reading CSV file: {}", csvFilePath, e);
            throw e;
        }
        log.info("Loaded {} employees from CSV file.", employees.size());
        return employees;
    }
}
