package com.dafortch.io;

import com.dafortch.validation.EmployeeValidator;

public abstract class AbstractEmployeesLoader implements EmployeesLoader {

    protected EmployeeValidator employeeValidator;

    public AbstractEmployeesLoader(EmployeeValidator employeeValidator) {
        this.employeeValidator = employeeValidator;
    }
}
