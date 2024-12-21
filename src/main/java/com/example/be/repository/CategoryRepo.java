package com.example.be.repository;

import com.example.be.entity.Category;
import com.example.be.payload.CategoryPayload;
import com.example.be.statics.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findAllByStatus(Status status);

    @Query("select c from Category c" +
            " WHERE ( :#{#filter.status} is null or c.status = :#{#filter.status} )" +
            " AND  ( :#{#filter.name} is null or c.name LIKE %:#{#filter.name}% )")
    List<Category> findAll(CategoryPayload filter);
}
