package com.dafortch.ui;

import javax.swing.table.AbstractTableModel;

import com.dafortch.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {

    private List<Employee> employees = new ArrayList<>();
    private final String[] columnNames = {"ID", "First Name", "Surname", "Email", "Salary"};

    public EmployeeTableModel(List<Employee> employees) {
        this.employees = employees;
    }

    public EmployeeTableModel() {
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        fireTableDataChanged();
    }

    public Employee getEmployeeAt(int rowIndex) {
        return employees.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee employee = employees.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> employee.getId();
            case 1 -> employee.getFirstName();
            case 2 -> employee.getSurName();
            case 3 -> employee.getEmail();
            case 4 -> employee.getSalary();
            default -> null;
        };
    }

}
