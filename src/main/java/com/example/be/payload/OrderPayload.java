package com.example.be.payload;

import com.example.be.entity.DeliveryInformationEntity;
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
public class OrderPayload {
    private String paymentMethod;
    private DeliveryInformationEntity deliveryInformation;
    private List<OrderDetailEntity> orderDetailEntities;
}
