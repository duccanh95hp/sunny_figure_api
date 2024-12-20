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
    @Query("select o from OrderEntity o " +
            "WHERE ( :#{#filter.status} is null or o.status = :#{#filter.status} )" +
            "AND ( :#{#filter.userId} = 0L or o.userId = :#{#filter.userId})" +
            "AND ( :#{#filter.fromDate} is null or o.createdAt >=  :#{#filter.fromDate} )" +
            "AND ( :#{#filter.toDate} is null or o.createdAt <=  :#{#filter.toDate})" +
            "ORDER BY o.updatedAt DESC")
    Page<OrderEntity> getAllAndSearch(@Param("filter") OrderFilter filter, Pageable pageable);

    List<OrderEntity> findAllByAffiliateCodeAndStatus(String affiliateCode, String status);
}
