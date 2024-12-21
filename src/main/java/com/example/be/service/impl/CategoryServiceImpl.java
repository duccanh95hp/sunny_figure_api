package com.example.be.service.impl;

import com.example.be.entity.Category;
import com.example.be.payload.CategoryPayload;
import com.example.be.repository.CategoryRepo;
import com.example.be.service.CategoryService;
import com.example.be.statics.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    @Override
    public Category save(CategoryPayload payload) {
        Category categoryModel = Category.builder()
                .name(payload.getName())
                .description(payload.getDescription())
                .status(payload.getStatus())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        return categoryRepo.save(categoryModel);
    }

    @Override
    public Category update(CategoryPayload payload, Long id) {
        Category category = findById(id);
        if(category != null){
            category.setName(payload.getName().isEmpty() ? category.getName() : payload.getName());
            category.setStatus(payload.getStatus());
            category.setDescription(payload.getDescription().isEmpty() ? category.getDescription() : payload.getDescription());
            category.setUpdatedAt(LocalDateTime.now());
            return categoryRepo.save(category);
        }
        return null;
    }

    @Override
    public boolean delete(Long aLong) {
        Category category = findById(aLong);
        if(category != null){
            category.setStatus(Status.INACTIVE);
            category.setUpdatedAt(LocalDateTime.now());
            categoryRepo.save(category);
            return true;
        }
        return false;
    }

    @Override
    public Category findById(Long aLong) {
        Category category = categoryRepo.findById(aLong).orElse(null);
        if(category != null){
            return category;
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }


    @Override
    public List<Category> findAll(CategoryPayload payload) {
        return categoryRepo.findAll(payload);
    }
}
