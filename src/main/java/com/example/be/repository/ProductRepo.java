package com.example.be.repository;

import com.example.be.entity.Product;
import com.example.be.payload.FilterPayload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("SELECT pro FROM Product pro " +
            "WHERE (:#{#filter.name} IS NULL OR LOWER(pro.name) LIKE LOWER(CONCAT('%', :#{#filter.name}, '%'))) " +
            "AND (:#{#filter.priceTo} = 0d OR pro.price <= :#{#filter.priceTo}) " +
            "AND (:#{#filter.priceFrom} = 0d OR pro.price >= :#{#filter.priceFrom}) " +
            "AND (:#{#filter.categoryId} IS NULL OR :#{#filter.categoryId} = 0 OR pro.category.id = :#{#filter.categoryId}) " + // Lọc theo categoryId nếu có
            "AND (:#{#filter.type} IS NULL OR pro.type = :#{#filter.type}) " +
            "AND pro.isDelete = false ORDER BY pro.id DESC")
    Page<Product> getAllAndSearch(@Param("filter") FilterPayload filter, Pageable pageable);


}
