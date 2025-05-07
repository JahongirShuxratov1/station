package com.example.stations.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    @NotBlank(message = "email cannot be null or empty")
    private String email;
    @NotBlank(message = "password cannot be null or empty")
    private String password;
    private String deviceId;
}
