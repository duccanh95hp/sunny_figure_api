package com.example.be.repository;

import com.example.be.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetailEntity, Long> {
    List<OrderDetailEntity> findByOrderId(long orderId);
}
