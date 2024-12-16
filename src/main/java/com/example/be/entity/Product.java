package com.example.be.entity;

import com.example.be.statics.Status;
import com.example.be.statics.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "price")
    private Double price;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "stock_quantity", nullable = false)
    private Double stockQuantity;

    @Column(name = "manufacturer", nullable = false)//
    private String manufacturer;

    @Column(name = "height", nullable = false)
    private String height;

    @Column(name = "weight", nullable = false)
    private String weight;

    @Column(name = "accessory", nullable = false)// Phụ kiện
    private String accessory;

    @Column(name = "box", nullable = false) // hộp
    private String box;

    @Column(name = "url_avatar", nullable = false) // Ảnh đại diện
    private String avatarUrl;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "type")
    private Type type;

}
