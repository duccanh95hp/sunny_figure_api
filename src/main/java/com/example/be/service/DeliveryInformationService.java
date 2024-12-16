package com.example.be.service;

import com.example.be.entity.DeliveryInformationEntity;

import java.util.List;

public interface DeliveryInformationService {
    List<DeliveryInformationEntity> getAllByUser();
}
