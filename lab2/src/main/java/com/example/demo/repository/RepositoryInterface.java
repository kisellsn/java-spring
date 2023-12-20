package com.example.demo.repository;

import java.util.List;

public interface RepositoryInterface<T> {

    int add(T item);
    void update(T item);

    List<T> findAll();

    void deleteById(int id);

    T findById(int id);

    T findByName(String name);

//    int deleteAll();
}
