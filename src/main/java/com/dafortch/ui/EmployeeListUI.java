package com.dafortch.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dafortch.io.CsvEmployeesLoader;
import com.dafortch.io.JsonEmployeesLoader;
import com.dafortch.model.Employee;
import com.dafortch.repository.Repository;
import com.dafortch.util.SQLUtils;
import com.dafortch.validation.EmployeeValidationException;
import com.dafortch.validation.EmployeeValidator;

public class EmployeeListUI extends JFrame {

    private static final Logger log = LogManager.getLogger(EmployeeListUI.class);

    private final Repository<Employee> employeeRepository;
    private final EmployeeValidator employeeValidator;
    private final CsvEmployeesLoader csvEmployeesLoader;
    private final JsonEmployeesLoader jsonEmployeesLoader;

    protected final EmployeeTableModel tableModel;
    protected final JTextField firstNameField;
    protected final JTextField surNameField;
    protected final JTextField emailField;
    protected final JTextField salaryField;
    private final JTable table;
    private final JButton updateButton;
    private final JButton deleteButton;

    protected Employee selectedEmployee;

    public EmployeeListUI(Repository<Employee> employeeRepository, EmployeeValidator employeeValidator,
                          CsvEmployeesLoader csvEmployeesLoader, JsonEmployeesLoader jsonEmployeesLoader) {
        this.employeeValidator = employeeValidator;
        this.csvEmployeesLoader = csvEmployeesLoader;
        this.jsonEmployeesLoader = jsonEmployeesLoader;
        this.employeeRepository = employeeRepository;
        setTitle("Employee List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new EmployeeTableModel();
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel filePanel = new JPanel(new GridLayout(2, 2));
        filePanel.setBorder(new TitledBorder("Load Employees from File"));
        JButton loadCsvButton = new JButton("Load CSV");
        loadCsvButton.addActionListener(e -> loadCsvFile());
        filePanel.add(loadCsvButton);

        JButton loadJsonButton = new JButton("Load JSON");
        loadJsonButton.addActionListener(e -> loadJsonFile());
        filePanel.add(loadJsonButton);

        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        formPanel.setBorder(new TitledBorder("Employee Management"));
        firstNameField = new JTextField();
        surNameField = new JTextField();
        emailField = new JTextField();
        salaryField = new JTextField();

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surNameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Salary:"));
        formPanel.add(salaryField);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> addEmployee());
        formPanel.add(addButton);

        updateButton = new JButton("Update Employee");
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateEmployee());
        formPanel.add(updateButton);

        deleteButton = new JButton("Delete Employee");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteEmployee());
        formPanel.add(deleteButton);

        JButton refreshButton = new JButton("Refresh List");
        refreshButton.addActionListener(e -> loadEmployees());
        formPanel.add(refreshButton);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(formPanel);
        bottomPanel.add(filePanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                selectedEmployee = tableModel.getEmployeeAt(selectedRow);
                loadEmployeeIntoForm(selectedEmployee);
                updateButton.setEnabled(true);
                deleteButton.setEnabled(true);
                log.trace("Selected employee loaded into form: {}", selectedEmployee);
            } else {
                selectedEmployee = null;
                clearForm();
                updateButton.setEnabled(false);
                deleteButton.setEnabled(false);
                log.trace("No employee selected. Form cleared.");
            }
        });

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() != -1) {
                    table.clearSelection(); // Deselect any selected rows
                    selectedEmployee = null;
                    clearForm();
                    updateButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    log.trace("Table deselected. Employee form cleared.");
                }
            }
        });

        loadEmployees();
    }

    private void loadCsvFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            log.debug("CSV file selected: {}", selectedFile.getPath());
            try {
                List<Employee> employees = csvEmployeesLoader.loadEmployees(selectedFile.getPath());

                log.info("Loaded employees from CSV file: {}", employees);

                showEmployeeConfirmationDialog(employees);
            } catch (Exception e) {
                Object errorMessage = e instanceof EmployeeValidationException ? e.getMessage() : e;
                log.error("Error loading employees from CSV file: ", e);
                JOptionPane.showMessageDialog(this, "Error loading employees from csv:\n\n"+errorMessage);
            }
        }
    }

    private void loadJsonFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            log.debug("JSON file selected: {}", selectedFile.getPath());
            try {
                List<Employee> employees = jsonEmployeesLoader.loadEmployees(selectedFile.getPath());

                log.info("Loaded employees from JSON file: {}", employees);

                showEmployeeConfirmationDialog(employees);
            } catch (Exception e) {
                Object errorMessage = e instanceof EmployeeValidationException ? e.getMessage() : e;
                log.error("Error loading employees from JSON file: ", e);
                JOptionPane.showMessageDialog(this, "Error loading employees from json:\n\n"+errorMessage);
            }
        }
    }

    private void showEmployeeConfirmationDialog(List<Employee> employees) {
        JTable confirmationTable = new JTable(new EmployeeTableModel(employees));
        confirmationTable.getTableHeader().setReorderingAllowed(false);
        confirmationTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(confirmationTable);

        int result = JOptionPane.showConfirmDialog(this, scrollPane,
                "Confirm Employee Data",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                employeeRepository.insertAll(employees);

                log.info("Employees added successfully.");
                JOptionPane.showMessageDialog(this, "Employees added successfully!");
                loadEmployees();
            } catch (SQLException e) {
                log.error("Error saving employees: ", e);
                JOptionPane.showMessageDialog(this, "Error adding employees: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadEmployeeIntoForm(Employee employee) {
        firstNameField.setText(employee.getFirstName());
        surNameField.setText(employee.getSurName());
        emailField.setText(employee.getEmail());
        salaryField.setText(String.valueOf(employee.getSalary()));
        log.trace("Employee loaded into form: {}", employee);
    }

    protected void addEmployee() {
        String firstName = firstNameField.getText();
        String surName = surNameField.getText();
        String email = emailField.getText();
        String salaryText = salaryField.getText();
        log.trace("Attempting to add employee with data - First Name: {}, Surname: {}, Email: {}, Salary: {}", firstName, surName, email, salaryText);
        try {
            try {

                employeeValidator.validateEmployeeValues(firstName, surName, email, salaryText);

                float salary = Float.parseFloat(salaryText);

                Employee employee = new Employee(firstName, surName, email, salary);
                employeeRepository.insert(employee);
                log.info("Employee added successfully: {}", employee);
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                clearForm();
                loadEmployees();
            } catch (SQLException e) {
                if (SQLUtils.isUniqueConstraintViolation(e)) {
                    log.warn("Add employee failed: Employee with email {} already exists", email);
                    throw new EmployeeValidationException("Email", "must be unique");
                } else {
                    log.error("Error saving employee: ", e);
                    JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EmployeeValidationException e) {
            log.warn("Validation failed for add employee: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void updateEmployee() {
        if (selectedEmployee == null) {
            log.warn("No employee selected for update");
            JOptionPane.showMessageDialog(this, "No employee selected for update", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String firstName = firstNameField.getText();
        String surName = surNameField.getText();
        String email = emailField.getText();
        String salaryText = salaryField.getText();
        log.trace("Attempting to update employee with data - First Name: {}, Surname: {}, Email: {}, Salary: {}", firstName, surName, email, salaryText);
        try {
            try {
                employeeValidator.validateEmployeeValues(firstName, surName, email, salaryText);

                float salary = Float.parseFloat(salaryText);

                selectedEmployee.setFirstName(firstName);
                selectedEmployee.setSurName(surName);
                selectedEmployee.setEmail(email);
                selectedEmployee.setSalary(salary);

                employeeRepository.update(selectedEmployee);
                log.info("Employee updated successfully: {}", selectedEmployee);
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                clearForm();
                loadEmployees();
            } catch (SQLException e) {
                if (SQLUtils.isUniqueConstraintViolation(e)) {
                    log.warn("Update employee failed: Employee with email {} already exists", email);
                    throw new EmployeeValidationException("Email", "Employee email must be unique");
                } else {
                    log.error("Error updating employee: ", e);
                    JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (EmployeeValidationException e) {
            log.warn("Validation failed for update employee: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void deleteEmployee() {
        if (selectedEmployee == null) {
            log.warn("No employee selected for deletion");
            JOptionPane.showMessageDialog(this, "No employee selected for deletion", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                employeeRepository.delete(selectedEmployee.getId());
                log.info("Employee deleted successfully: {}", selectedEmployee);
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
                clearForm();
                loadEmployees();
            } catch (SQLException e) {
                log.error("Error deleting employee: ", e);
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        firstNameField.setText("");
        surNameField.setText("");
        emailField.setText("");
        salaryField.setText("");
        selectedEmployee = null;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        log.trace("Form cleared and selection reset.");
    }

    protected void loadEmployees() {
        try {
            List<Employee> employees = employeeRepository.findAll();
            tableModel.setEmployees(employees);
            log.info("Employees loaded successfully. Number of employees: {}", employees.size());
        } catch (SQLException e) {
            log.error("Error loading employees: ", e);
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
