package com.dfortch18.validation;

public class EmployeeValidationException extends RuntimeException {

    private final String fieldName;

    private final String validationError;

    public EmployeeValidationException(String fieldName, String validationError) {
        super(fieldName + ": " + validationError);
        this.fieldName = fieldName;
        this.validationError = validationError;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValidationError() {
        return validationError;
    }
}
