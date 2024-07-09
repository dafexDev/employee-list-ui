package com.dfortch18.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.dfortch18.model.Employee;

public interface Repository<T> {
    List<T> findAll() throws SQLException;

    Optional<Employee> getById(Integer id) throws SQLException;

    void insert(T entity) throws SQLException;

    void insertAll(Iterable<T> entities) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(Integer id) throws SQLException;
}
