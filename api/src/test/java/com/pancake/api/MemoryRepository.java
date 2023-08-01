package com.pancake.api;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class MemoryRepository<T> {
    private final Map<Long, T> data;

    protected MemoryRepository(Map<Long, T> data) {
        this.data = data;
    }

    public MemoryRepository() {
        this(new HashMap<>());
    }

    public T save(T entity) {
        assert entity != null;

        var id = getId(entity);
        if (id == null) {
            id = nextId();
            setId(entity, id);
        }
        data.put(id, entity);

        return entity;
    }

    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public Optional<T> findById(long id) {
        return Optional.ofNullable(data.get(id));
    }

    private void setId(T entity, long id) {
        ReflectionTestUtils.setField(entity, "id", id);
    }

    private Long getId(T entity) {
        return (Long) ReflectionTestUtils.getField(entity, "id");
    }

    private long nextId() {
        return data.size() + 1L;
    }
}
