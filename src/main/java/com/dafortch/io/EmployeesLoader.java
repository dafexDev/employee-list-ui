package com.dafortch.io;

import java.util.List;

import com.dafortch.model.Employee;

public interface EmployeesLoader {
    List<Employee> loadEmployees(String filePath) throws Exception;
}
