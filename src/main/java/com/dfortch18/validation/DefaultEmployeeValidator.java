package com.dfortch18.validation;

import java.util.Map;

import com.dfortch18.util.ValidationUtils;

public class DefaultEmployeeValidator implements EmployeeValidator {
    @Override
    public void validateEmployeeValues(String firstName, String surName, String email, String salaryText) {
        Map<String, String> fieldValues = Map.of(
                "First Name", firstName,
                "Surname", surName,
                "Email", email,
                "Salary", salaryText
        );

        boolean allFieldsEmpty = fieldValues.values().stream().allMatch(String::isEmpty);
        if (allFieldsEmpty) {
            throw new EmployeeValidationException("All Fields", "must be filled out");
        }

        fieldValues.forEach((fieldName, value) -> {
            if (value.isEmpty()) {
                throw new EmployeeValidationException(fieldName, "must be filled out");
            }
        });

        if (!ValidationUtils.onlyLetters(firstName)) {
            throw new EmployeeValidationException("First Name", "must contain only letters");
        }

        if (!ValidationUtils.onlyLetters(surName)) {
            throw new EmployeeValidationException("Sur Name", "must contain only letters");
        }

        if (!ValidationUtils.email(email)) {
            throw new EmployeeValidationException("Email", "Invalid email format");
        }

        if (!ValidationUtils.floatParsable(salaryText)) {
            throw new EmployeeValidationException("Salary", "Salary must be a positive number");
        }

        if (!ValidationUtils.greaterThanZero(Float.parseFloat(salaryText))) {
            throw new EmployeeValidationException("Salary", "Salary must be a valid number");
        }

    }

    @Override
    public void validateEmployeeValues(String firstName, String surName, String email, Float salary) {
        Map<String, String> fieldValues = Map.of(
                "First Name", firstName,
                "Surname", surName,
                "Email", email
        );

        fieldValues.forEach((fieldName, value) -> {
            if (value.isEmpty()) {
                throw new EmployeeValidationException(fieldName, "must be filled out");
            }
        });

        if (!ValidationUtils.onlyLetters(firstName)) {
            throw new EmployeeValidationException("First Name", "must contain only letters");
        }

        if (!ValidationUtils.onlyLetters(surName)) {
            throw new EmployeeValidationException("Sur Name", "must contain only letters");
        }

        if (!ValidationUtils.email(email)) {
            throw new EmployeeValidationException("Email", "Invalid email format");
        }

        if (!ValidationUtils.greaterThanZero(salary)) {
            throw new EmployeeValidationException("Salary", "Salary must be a valid number");
        }

    }
}
