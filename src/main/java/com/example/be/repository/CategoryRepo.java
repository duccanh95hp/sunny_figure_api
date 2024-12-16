package com.example.be.repository;

import com.example.be.entity.Category;
import com.example.be.statics.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findAllByStatus(Status status);
}
