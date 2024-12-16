package com.example.be.model;

import com.example.be.statics.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CategoryModel {
    private Long id;
    private String name;
    private Status status;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
