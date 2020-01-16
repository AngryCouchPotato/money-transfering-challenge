package com.challenge.moneytransferring.db.inmemory;

import com.challenge.moneytransferring.exception.EntityNotFountException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Storage <T>  {

    private ConcurrentMap<Long, T> entitiesById;
    private AtomicLong idCounter;

    public Storage() {
        this.entitiesById = new ConcurrentHashMap<>();
        this.idCounter = new AtomicLong();
    }

    public Optional<T> find(Long id) {
        return Optional.ofNullable(entitiesById.get(id));
    }

    public T get(Long id) {
        return find(id).orElseThrow(() -> new EntityNotFountException(String.format("Could not find Entity with id = %d", id), id));
    }

    public List<T> getAll() {
        return new ArrayList<>(entitiesById.values());
    }

    public T put(Long id, T value) {
        return entitiesById.put(id, value);
    }

    public long nextId() {
        return idCounter.incrementAndGet();
    }

}
