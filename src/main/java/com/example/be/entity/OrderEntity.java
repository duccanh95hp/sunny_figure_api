package com.example.be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "status")
    private String status;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "delivery_information_id")
    private Long deliveryInformationId;
    @Column(name = "reason")
    private String reason;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "order_code")
    private String orderCode;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;
    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;
}
