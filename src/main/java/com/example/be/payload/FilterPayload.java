package com.example.be.payload;

import com.example.be.statics.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FilterPayload {
    private String name;
    private int size;
    private int page;
    private String procedure;
    private double priceTo;
    private double priceFrom;
    private Long categoryId;
    private Type type;
    private String categoryName;
}
