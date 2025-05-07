package com.example.stations.controller;


import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.LoginDto;
import com.example.stations.dto.RegisterDto;
import com.example.stations.dto.VerifyDto;
import com.example.stations.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auths")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/loginByEmail")
    public ApiResponse loginByEmail(@Valid @RequestBody LoginDto dto) {
        return this.authService.loginByEmail(dto);
    }

    @PostMapping("/registerByEmail")
    public ApiResponse registerByEmail(@Valid @RequestBody RegisterDto dto) {
        return this.authService.registerByEmail(dto);
    }

    @PostMapping("/verify")
    public ApiResponse verify(@RequestBody VerifyDto dto) {
        return this.authService.verify(dto);
    }

    @PostMapping("/resendCodeToEmail")
    public ApiResponse resendCodeToEmail(@RequestParam("email") String email,
                                         @RequestParam("deviceId") String deviceId,
                                         @RequestParam("id") Long id) {
        return this.authService.resendCodeToEmail(email, deviceId, id);
    }

    @PostMapping("/checkCodeByEmail")
    public ApiResponse checkCodeByEmail(@RequestParam("email") String email,
                                        @RequestParam("code") String code,
                                        @RequestParam("deviceId") String deviceId) {
        return this.authService.checkCodeByEmail(email, code, deviceId);
    }
}
