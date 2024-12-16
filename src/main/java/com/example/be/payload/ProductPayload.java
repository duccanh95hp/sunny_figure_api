package com.example.be.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductPayload {
    private String name;
    private String description;
    private Double price;
    private MultipartFile file;

    private Long categoryId;

    private Double stockQuantity;

    private String manufacturer;

    private String height;

    private String weight;

    private String accessory;

    private String box;

    private String avatarUrl;

    private Boolean isDelete;

}
