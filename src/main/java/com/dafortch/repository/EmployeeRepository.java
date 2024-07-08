package com.dafortch.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dafortch.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository extends AbstractRepository<Employee> {

    private static final Logger log = LogManager.getLogger(EmployeeRepository.class);

    public EmployeeRepository(Connection connection) {
        super(connection);
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        log.trace("Entering findAll method");

        try (Statement stmt = connection.createStatement()) {
            log.debug("Executing SQL query: SELECT * FROM employees");
            ResultSet resSet = stmt.executeQuery("SELECT * FROM employees");

            while (resSet.next()) {
                Employee employee = createEmployee(resSet);
                employees.add(employee);
                log.trace("Employee retrieved from ResultSet: {}", employee);
            }
            log.info("Employees retrieved successfully. Number of employees: {}", employees.size());
        } catch (SQLException e) {
            log.error("Error retrieving employees: ", e);
            throw e;
        }

        log.trace("Exiting findAll method");
        return employees;
    }

    @Override
    public Optional<Employee> getById(Integer id) throws SQLException {
        Employee employee = null;
        log.trace("Entering getById method with ID: {}", id);

        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            log.debug("Executing SQL query: {}", sql);

            ResultSet resSet = stmt.executeQuery();
            if (resSet.next()) {
                employee = createEmployee(resSet);
                log.trace("Employee retrieved from ResultSet: {}", employee);
            }
        } catch (SQLException e) {
            log.error("Error retrieving employee by ID {}: ", id, e);
            throw e;
        }

        log.trace("Exiting getById method");
        return Optional.ofNullable(employee);
    }

    @Override
    public void insert(Employee employee) throws SQLException {
        log.trace("Entering insert method with Employee: {}", employee);
        String sql = "INSERT INTO employees (first_name, sur_name, email, salary) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getSurName());
            stmt.setString(3, employee.getEmail());
            stmt.setFloat(4, employee.getSalary());

            log.debug("Executing SQL query: {}", sql);
            stmt.executeUpdate();
            log.info("Employee inserted successfully: {}", employee);
        } catch (SQLException e) {
            log.error("Error inserting employee: ", e);
            throw e;
        }

        log.trace("Exiting insert method");
    }

    @Override
    public void insertAll(Iterable<Employee> employees) throws SQLException {
        log.trace("Entering insertAll method");
        for (Employee employee : employees) {
            log.debug("Inserting employee: {}", employee);
            this.insert(employee);
        }
        log.trace("Exiting insertAll method");
    }

    @Override
    public void update(Employee employee) throws SQLException {
        log.trace("Entering update method with Employee: {}", employee);
        String sql = "UPDATE employees SET first_name = ?, sur_name = ?, email = ?, salary = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getSurName());
            stmt.setString(3, employee.getEmail());
            stmt.setFloat(4, employee.getSalary());
            stmt.setInt(5, employee.getId());

            log.debug("Executing SQL query: {}", sql);
            stmt.executeUpdate();
            log.info("Employee updated successfully: {}", employee);
        } catch (SQLException e) {
            log.error("Error updating employee: ", e);
            throw e;
        }

        log.trace("Exiting update method");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        log.trace("Entering delete method with ID: {}", id);
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            log.debug("Executing SQL query: {}", sql);
            stmt.executeUpdate();
            log.info("Employee deleted successfully with ID: {}", id);
        } catch (SQLException e) {
            log.error("Error deleting employee: ", e);
            throw e;
        }

        log.trace("Exiting delete method");
    }

    private static Employee createEmployee(ResultSet resSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resSet.getInt("id"));
        employee.setFirstName(resSet.getString("first_name"));
        employee.setSurName(resSet.getString("sur_name"));
        employee.setEmail(resSet.getString("email"));
        employee.setSalary(resSet.getFloat("salary"));

        log.trace("Employee created from ResultSet: {}", employee);
        return employee;
    }
}
