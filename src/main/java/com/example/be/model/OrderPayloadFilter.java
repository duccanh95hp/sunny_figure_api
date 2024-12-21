package com.example.be.model;

import com.example.be.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderPayloadFilter {
    private String status;
    private String fromDate;
    private String toDate;
    private int size;
    private int page;
    private String procedure;
    private String note;

    private String reason;

    private String deliveryDate;

    private List<OrderDetailEntity> orderDetails;
    private int limit;
    private String orderCode;
    private String email;
    private String phone;
    private String name;
}