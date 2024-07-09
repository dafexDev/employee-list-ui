package com.dfortch18.io;

import java.util.List;

import com.dfortch18.model.Employee;

public interface EmployeesLoader {
    List<Employee> loadEmployees(String filePath) throws Exception;
}
