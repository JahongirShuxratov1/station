package com.example.stations.service.impl;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.LoginDto;
import com.example.stations.dto.RegisterDto;
import com.example.stations.dto.VerifyDto;
import com.example.stations.entity.*;
import com.example.stations.enums.UserStatus;
import com.example.stations.exceptions.ResourceNotFoundException;
import com.example.stations.mapper.UserMapper;
import com.example.stations.repository.*;
import com.example.stations.service.AuthService;
import com.example.stations.service.DeviceService;
import com.example.stations.util.JwtUtil;
import com.example.stations.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final DeviceRepository deviceRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CodeRepository codeRepository;
    private final UserRepository userRepository;
    private final DeviceService deviceService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final Utils utils;

    @Override
    public ApiResponse registerByEmail(RegisterDto dto) {

        User user = this.userRepository.findByEmail(dto.getEmail()).orElse(null);
        if (utils.existUsername(dto.getUsername())) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Username already exists")
                    .build();
        }
        if (user != null) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Username already exist")
                    .build();
        }
        if (!utils.checkEmail(dto.getEmail())) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Username email is invalid")
                    .build();
        }

        String code = utils.getCode();
        if (utils.sendCodeToMail(dto.getEmail(), code)) {
            try {
                UserRole role = this.roleRepository.findByName(dto.getRole()).orElse(null);
                User entity = this.userMapper.toEntityByEmail(dto);
                entity.setCreatedAt(LocalDateTime.now());
                entity.setStatus(UserStatus.PENDING);
                if (role != null) {
                    if (entity.getRole() != null && !entity.getRole().isEmpty()) {
                        entity.getRole().add(role);
                    } else {
                        Set<UserRole> set = new HashSet<>();
                        set.add(role);
                        entity.setRole(set);
                    }
                }
                User save = this.userRepository.save(entity);
                Device device = this.deviceService.addTrustedDevice(save.getId(), dto.getDeviceId());

                Code c = Code.builder()
                        .user(save)
                        .code(code)
                        .createdAt(LocalDateTime.now())
                        .device(device)
                        .build();
                this.codeRepository.save(c);

                Token jwt = Token.builder()
                        .user(save)
                        .type("Bearer")
                        .createdAt(LocalDateTime.now())
                        .build();
                this.tokenRepository.save(jwt);

                return ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Auth code sent")
                        .meta(Map.of("userId", save.getId()))
                        .build();

            } catch (Exception e) {
                return ApiResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(e.getMessage())
                        .build();
            }
        }
        return ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error sending auth code")
                .build();
    }

    @Override
    @Transactional
    public ApiResponse verify(VerifyDto dto) {
        User user = this.userRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Device dev = this.deviceRepository.findByUserIdAndDeviceName(user.getId(), dto.getDeviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));
        Map<String, Object> map = new HashMap<>();
        Code code = this.codeRepository.findByUserIdAndDeviceId(user.getId(), dev.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Code not found"));

        String email = dto.getEmail();
        if (email == null ) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("User email invalid")
                    .build();
        }


        if (!code.getCode().equals(dto.getCode())) {

            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Code error")
                    .build();
        }

        code.setApprovedAt(LocalDateTime.now());
        code.setDevice(dev);
        this.codeRepository.save(code);

        Token token = this.tokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setUsername(dto.getUsername());
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            user.setEmail(dto.getEmail());
        }
        // Remove this invalid line
        // user.setRole(user.getRole().add(dto.ge));
        User saveUser = this.userRepository.save(user);
        
        String generateToken = jwtUtil.generateToken(saveUser.getUsername());

        token.setToken(generateToken);
        token.setExpiredAt(LocalDateTime.now().plusDays(10));
        Token savedToken = this.tokenRepository.save(token);

        map.put("token", savedToken.getToken());
        map.put("userId", saveUser.getId());
        map.put("user", this.userMapper.toDto(saveUser));


        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("SUCCESS")
                .data(map)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse loginByEmail(LoginDto dto) {
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("User email invalid")
                    .build();
        }



        User user;
        try {
            user = this.userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (ResourceNotFoundException e) {

            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid authentication characters")
                    .build();
        }

        if (!utils.checkEmail(dto.getEmail())) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid credentials")
                    .build();
        }

        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            authenticationManager.authenticate(authentication);


        } catch (Exception e) {

            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid credentials")
                    .build();
        }

        Token token = this.tokenRepository.findByUserId(user.getId()).orElse(null);
        String userToken = null;
        if (token != null) {
            userToken = token.getToken();
            if (token.getExpiredAt().isBefore(LocalDateTime.now())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                userToken = jwtUtil.generateToken(userDetails.getUsername());
                token.setToken(userToken);
                token.setCreatedAt(LocalDateTime.now());
                token.setExpiredAt(LocalDateTime.now().plusDays(10));
                this.tokenRepository.save(token);
            }
        }
        Device trustedDevice = this.deviceRepository.findByUserIdAndDeviceName(user.getId(), dto.getDeviceId())
                .orElse(null);

        String code = utils.getCode();
        if (trustedDevice == null) {
            if (utils.sendCodeToMail(dto.getEmail(), code)) {

                Device device = new Device();
                device.setUser(user);
                device.setDeviceName(dto.getDeviceId());
                Device save = this.deviceRepository.save(device);

                Code c = new Code();
                c.setCreatedAt(LocalDateTime.now());
                c.setCode(code);
                c.setUser(user);
                c.setDevice(save);
                this.codeRepository.save(c);

                return ApiResponse.builder()
                        .code(0)
                        .status(HttpStatus.OK)
                        .message("Code sent")
                        .data(userToken)
                        .meta(Map.of("user", this.userMapper.toDto(user)))
                        .build();
            }
        }


        return ApiResponse.builder()
                .code(1)
                .status(HttpStatus.OK)
                .message("SUCCESS")
                .data(userToken)
                .meta(Map.of("user", this.userMapper.toDto(user)))
                .build();
    }

    @Override
    @Transactional
    public ApiResponse resendCodeToEmail(String email, String deviceId, Long id) {
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("User email invalid")
                    .build();
        }


        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Device device = this.deviceRepository.findByUserIdAndDeviceName(user.getId(), deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found"));


        String code = utils.getCode();
        if (utils.sendCodeToMail(email, code)) {
            Code c = this.codeRepository.findByUserIdAndDeviceId(user.getId(), device.getId()).orElse(null);
            if (c != null) {
                c.setCode(code);
                c.setCreatedAt(LocalDateTime.now());
                this.codeRepository.save(c);
            }
            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .message("Auth code sent")
                    .build();
        }
        return ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error while sending auth code")
                .build();
    }

    @Override
    public ApiResponse checkCodeByEmail(String email, String code, String deviceId) {
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("User email invalid")
                    .build();
        }



        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            Device device = this.deviceRepository.findByUserIdAndDeviceName(user.getId(), deviceId)
                    .orElse(null);
            if (device != null) {
                Code c = this.codeRepository.findByUserIdAndDeviceId(user.getId(), device.getId()).orElse(null);

                if (c != null && c.getDevice() != null && c.getDevice().getUser() != null
                        && c.getDevice().getUser().getId().equals(user.getId())) {
                    c.getDevice().setAddedAt(LocalDateTime.now());
                    c.getDevice().setDeviceName(deviceId);
                    c.getDevice().setExpiredAt(LocalDateTime.now().plusDays(15));
                    this.deviceRepository.save(c.getDevice());
                }

                if (!this.utils.checkCode(email, code, device.getId())) {

                    return ApiResponse.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message("Error")
                            .build();
                }

                Map<String, Object> map = new HashMap<>();
                Device devices = this.deviceService.addTrustedDeviceByLogin(user.getId(), deviceId);
                devices.setUser(user);
                devices.setDeviceName(deviceId);
                Device save = this.deviceRepository.save(devices);

                Code cc = this.codeRepository.findByUserIdAndDeviceId(user.getId(), device.getId()).orElse(null);
                if (cc != null) {
                    cc.setApprovedAt(LocalDateTime.now());
                    cc.setDevice(save);
                    this.codeRepository.save(cc);
                }

                Token token = this.tokenRepository.findByUserId(user.getId()).orElse(null);
                map.put("token", token != null ? token.getToken() : "");
                map.put("user", this.userMapper.toDto(user));


                return ApiResponse.builder()
                        .status(HttpStatus.OK)
                        .message("Authentication failed")
                        .data(map)
                        .build();
            }
        }


        return ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("User not found")
                .build();
    }

}