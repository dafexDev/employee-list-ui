package com.dfortch18.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dfortch18.model.Employee;
import com.dfortch18.repository.EmployeeRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EmployeeRepositoryTest {

    private Connection connection;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        Statement stmt = connection.createStatement();
        stmt.execute("DROP TABLE IF EXISTS employees");
        stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(255), " +
                "sur_name VARCHAR(255), " +
                "email VARCHAR(255), " +
                "salary FLOAT)");
        employeeRepository = new EmployeeRepository(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    public void testFindAll() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO employees (first_name, sur_name, email, salary) " +
                "VALUES ('John', 'Doe', 'john.doe@example.com', 50000)");

        List<Employee> expectedEmployees = new ArrayList<>();
        expectedEmployees.add(new Employee(1, "John", "Doe", "john.doe@example.com", 50000f));

        List<Employee> employees = employeeRepository.findAll();

        assertEquals(expectedEmployees, employees);
    }

    @Test
    public void testGetById() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO employees (first_name, sur_name, email, salary) " +
                "VALUES ('John', 'Doe', 'john.doe@example.com', 50000)");

        Employee expectedEmployee = new Employee(1, "John", "Doe", "john.doe@example.com", 50000f);

        Optional<Employee> employee = employeeRepository.getById(1);

        assertTrue(employee.isPresent());
        assertEquals(expectedEmployee, employee.get());
    }

    @Test
    public void testGetByIdNotFound() throws SQLException {
        Optional<Employee> employee = employeeRepository.getById(999);

        assertFalse(employee.isPresent());
    }

    @Test
    public void testInsert() throws SQLException {
        Employee newEmployee = new Employee("Jane", "Smith", "jane.smith@example.com", 60000f);

        employeeRepository.insert(newEmployee);

        List<Employee> employees = employeeRepository.findAll();
        Employee expectedEmployee = new Employee(1, "Jane", "Smith", "jane.smith@example.com", 60000f);
        assertEquals(List.of(expectedEmployee), employees);
    }

    @Test
    public void testUpdate() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO employees (first_name, sur_name, email, salary) " +
                "VALUES ('John', 'Doe', 'john.doe@example.com', 50000)");
        Employee updatedEmployee = new Employee(1, "John", "Doe", "john.doe@example.com", 55000f);

        employeeRepository.update(updatedEmployee);

        Optional<Employee> employee = employeeRepository.getById(1);
        assertTrue(employee.isPresent());
        assertEquals(updatedEmployee, employee.get());
    }

    @Test
    public void testDelete() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("INSERT INTO employees (first_name, sur_name, email, salary) " +
                "VALUES ('John', 'Doe', 'john.doe@example.com', 50000)");

        employeeRepository.delete(1);

        List<Employee> employees = employeeRepository.findAll();
        assertTrue(employees.isEmpty());
    }
}
