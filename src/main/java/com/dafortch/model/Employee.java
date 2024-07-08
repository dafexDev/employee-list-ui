package com.dafortch.model;

import java.util.Objects;

public class Employee {

    private Integer id;

    private String firstName;

    private String surName;

    private String email;

    private Float salary;

    public Employee(Integer id, String firstName, String surName, String email, Float salary) {
        this.id = id;
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.salary = salary;
    }

    public Employee(String firstName, String surName, String email, Float salary) {
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
        this.salary = salary;
    }

    public Employee() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) && Objects.equals(surName, employee.surName) && Objects.equals(email, employee.email) && Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, surName, email, salary);
    }
}
