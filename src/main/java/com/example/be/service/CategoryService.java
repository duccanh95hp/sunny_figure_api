package com.example.be.service;

import com.example.be.entity.Category;
import com.example.be.payload.CategoryPayload;
import com.example.be.statics.Status;

import java.util.List;

public interface CategoryService extends CrudService<CategoryPayload, Category, Long>{

    List<Category> findAllByStatus(Status status);
}
