package com.tinkoff_lab.dao;

public interface DAO<T> {      // I decided its a good idea creating an interface for dao
    public void insert(T entity);
}
