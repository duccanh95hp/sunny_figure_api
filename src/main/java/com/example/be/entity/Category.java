package com.example.be.entity;

import com.example.be.statics.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "status")
    private Status status;
    @NotNull
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
