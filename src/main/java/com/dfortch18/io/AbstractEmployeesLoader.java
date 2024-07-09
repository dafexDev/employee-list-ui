package com.dfortch18.io;

import com.dfortch18.validation.EmployeeValidator;

public abstract class AbstractEmployeesLoader implements EmployeesLoader {

    protected EmployeeValidator employeeValidator;

    public AbstractEmployeesLoader(EmployeeValidator employeeValidator) {
        this.employeeValidator = employeeValidator;
    }
}
