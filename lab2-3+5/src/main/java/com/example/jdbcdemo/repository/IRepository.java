package com.example.jdbcdemo.repository;

import java.util.List;

public interface IRepository<T> {
    int add(T item);
    void update(T item);
    T findById(int id);
    T findByName(String name);
    List<T> findAll();
    int deleteById(int id);
    int deleteAll();
}
