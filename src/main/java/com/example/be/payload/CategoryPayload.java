package com.example.be.payload;

import com.example.be.statics.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CategoryPayload {
    private String name;
    private String description;
    private Status status;
}
