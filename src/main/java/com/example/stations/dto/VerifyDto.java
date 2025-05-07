package com.example.stations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyDto {
    private Long id;
    private String code;
    private String email;
    private String password;
    private String deviceId;
    private String username;
}
