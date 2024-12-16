package com.example.be.service;

import com.google.api.gax.paging.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<T, D, ID> {
    D save(T payload);
    D update(T payload,ID id);
    boolean delete(ID id);
    D findById(ID id);
    List<D> findAll();
}
