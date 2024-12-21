package com.example.be.repository;

import com.example.be.entity.OrderEntity;
import com.example.be.model.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);
    @Query("SELECT o FROM OrderEntity o " +
            "LEFT JOIN User u ON o.userId = u.id " +
            "WHERE (:#{#filter.status} IS NULL OR o.status = :#{#filter.status}) " +
            "AND (:#{#filter.userId} = 0L OR o.userId = :#{#filter.userId}) " +
            "AND (:#{#filter.fromDate} IS NULL OR o.createdAt >= :#{#filter.fromDate}) " +
            "AND (:#{#filter.toDate} IS NULL OR o.createdAt <= :#{#filter.toDate}) " +
            "AND (:#{#filter.orderCode} IS NULL OR o.orderCode LIKE %:#{#filter.orderCode}%) " +
            "AND (:#{#filter.name} IS NULL OR u.username LIKE %:#{#filter.name}%) " +
            "AND (:#{#filter.phone} IS NULL OR u.telephone LIKE %:#{#filter.phone}%) " +
            "AND (:#{#filter.email} IS NULL OR u.email LIKE %:#{#filter.email}%) " +
            "ORDER BY o.updatedAt DESC")
    Page<OrderEntity> getAllAndSearch(@Param("filter") OrderFilter filter, Pageable pageable);


    List<OrderEntity> findAllByAffiliateCodeAndStatus(String affiliateCode, String status);
}
