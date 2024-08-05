package com.tinkoff_lab.dao;

public interface DAO<T> {      // I decided its a good idea creating an interface for dao
    public int insert(T entity);
    public T findByID(int id);
}
