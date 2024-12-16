package com.example.be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderFilter {
    private long userId;
    private String status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int size;
    private int page;
    private int limit;
}
