package com.example.be.service;

import com.example.be.common.Page;
import com.example.be.dto.OrderDetailStatusDto;
import com.example.be.entity.OrderEntity;
import com.example.be.model.OrderPayloadFilter;
import com.example.be.payload.OrderPayload;
import com.example.be.payload.StatusPayload;
import com.example.be.statics.Status;

import java.util.List;

public interface OrderService {
    OrderEntity createOrder(OrderPayload payload);
    boolean updateStatus(StatusPayload payload);

    List<OrderEntity> findByUser();

    Page<Object> getAllAndSearch(OrderPayloadFilter filter);
    OrderDetailStatusDto detailOrder(long orderId);

    boolean updateStatus(Long orderId, Status status);

}
