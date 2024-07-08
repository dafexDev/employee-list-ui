package com.dafortch.io;

import com.dafortch.model.Employee;
import com.dafortch.validation.EmployeeValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class JsonEmployeesLoader extends AbstractEmployeesLoader {

    private static final Logger log = LogManager.getLogger(JsonEmployeesLoader.class);
    private final Gson gson = new Gson();

    public JsonEmployeesLoader(EmployeeValidator employeeValidator) {
        super(employeeValidator);
    }

    @Override
    public List<Employee> loadEmployees(String filePath) throws Exception {
        log.trace("Starting to load JSON file: {}", filePath);
        try (FileReader reader = new FileReader(filePath)) {
            Type employeeListType = new TypeToken<List<Employee>>() {}.getType();
            List<Employee> employees = gson.fromJson(reader, employeeListType);
            log.debug("Parsed JSON file into employee list with {} employees.", employees.size());

            for (Employee employee : employees) {
                try {
                    employeeValidator.validateEmployeeValues(employee.getFirstName(), employee.getSurName(), employee.getEmail(), employee.getSalary());
                    log.debug("Validated employee: {}", employee);
                } catch (Exception e) {
                    log.warn("Validation failed for employee: {}", employee, e);
                    throw e;
                }
            }

            log.info("Loaded {} employees from JSON file.", employees.size());
            return employees;
        } catch (Exception e) {
            log.error("Error reading JSON file: {}", filePath, e);
            throw e;
        }
    }
}
