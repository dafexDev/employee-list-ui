package com.dfortch18.validation;

public interface EmployeeValidator {

    void validateEmployeeValues(String firstName, String surName, String email, String salaryText);

    void validateEmployeeValues(String firstName, String surName, String email, Float salary);
}
