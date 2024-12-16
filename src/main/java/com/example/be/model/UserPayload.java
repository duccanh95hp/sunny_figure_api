package com.example.be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserPayload {
    private Long id;
    private String username;
    private String email;
    private String birthday;
    private String telephone;
    private String fullName;
    private String address;
}
