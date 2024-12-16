package com.example.be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "address")
    private String address;
    @Column(name = "name")
    private String name;
    @Column(name = "level_prior")
    private Long levelPrior;
    @Column(name = "user_id")
    private Long userId;

}
