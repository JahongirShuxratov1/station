package com.example.stations.service;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.LoginDto;
import com.example.stations.dto.RegisterDto;
import com.example.stations.dto.VerifyDto;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {

    ApiResponse loginByEmail(LoginDto dto);

    ApiResponse registerByEmail(RegisterDto dto);

    ApiResponse verify(VerifyDto dto);

    ApiResponse resendCodeToEmail(String email,String deviceId,Long id);

    ApiResponse checkCodeByEmail(String email, String code,String deviceId);
}
