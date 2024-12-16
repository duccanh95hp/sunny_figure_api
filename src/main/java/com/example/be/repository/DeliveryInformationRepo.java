package com.example.be.repository;

import com.example.be.entity.DeliveryInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryInformationRepo extends JpaRepository<DeliveryInformationEntity, Long> {
    List<DeliveryInformationEntity> findAllByUserId(Long userId);
}
