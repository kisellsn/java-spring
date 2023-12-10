package com.example.demo.repositories;

import java.util.List;

public interface RepositoryInterface<T> {

    T save(T item);

    List<T> findAll();

    void delete(T item);

    T findById(Long id);

    T find(String name);
}
