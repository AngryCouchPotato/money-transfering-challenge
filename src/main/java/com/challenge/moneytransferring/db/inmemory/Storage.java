package com.challenge.moneytransferring.db.inmemory;

import com.challenge.moneytransferring.exception.EntityNotFountException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Storage <T>  {

    private ConcurrentMap<Long, T> concurrentMap;

    public Storage() {
        this.concurrentMap = new ConcurrentHashMap<>();
    }

    public Optional<T> find(Long id) {
        return Optional.ofNullable(concurrentMap.get(id));
    }

    public T get(Long id) {
        return find(id).orElseThrow(() -> new EntityNotFountException(String.format("Could not find Entity with id = %d", id), id));
    }

    public List<T> getAll() {
        return new ArrayList<>(concurrentMap.values());
    }

    public T put(Long id, T value) {
        return concurrentMap.put(id, value);
    }

    public T remove(Long id) {
        return concurrentMap.remove(id);
    }

}
