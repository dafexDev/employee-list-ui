package com.dafortch.repository;

import java.sql.Connection;

public abstract class AbstractRepository<T> implements Repository<T> {

    protected final Connection connection;

    public AbstractRepository(Connection connection) {
        this.connection = connection;
    }
}
