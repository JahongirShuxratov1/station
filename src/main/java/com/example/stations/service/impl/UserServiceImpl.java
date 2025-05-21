package com.example.stations.service.impl;

import com.example.stations.dto.ApiResponse;
import com.example.stations.entity.Token;
import com.example.stations.entity.User;
import com.example.stations.entity.UserRole;
import com.example.stations.exceptions.ResourceNotFoundException;
import com.example.stations.mapper.UserMapper;
import com.example.stations.repository.RoleRepository;
import com.example.stations.repository.TokenRepository;
import com.example.stations.repository.UserRepository;
import com.example.stations.service.UserService;
import com.example.stations.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final Utils utils;
    @Override
    public ApiResponse updateUserNameAndFullName(Long userId, String username, String fullname) {
        User user = this.userRepository.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (utils.existUsername(username)) {
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("username already exists")
                    .build();
        }
        user.setUsername(username);
        user.setFullname(fullname);
        this.userRepository.save(user);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("User updated successfully")
                .build();
    }

    @Override
    public ApiResponse addRoleToUser(Long userId, Long roleId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserRole role = this.roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            user.getRole().add(role);
        } else {
            user.setRole(new HashSet<>(Set.of(role)));
        }
        this.userRepository.save(user);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Role added to user successfully")
                .build();
    }

    @Override
    public ApiResponse deleteRoleFromUser(Long userId, Long roleId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        this.roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        this.userRepository.deleteRoleFromUser(userId, roleId);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("Delete role from user")
                .build();
    }

    @Override
    public ApiResponse getUserById(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("SUCCESS")
                .data(this.userMapper.toDto(user))
                .build();
    }


    @Override
    public ApiResponse getAllActiveUser(Pageable pageable) {
        Page<User> page = this.userRepository.getAllActiveUsers(pageable);
        if (page != null && !page.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(this.userMapper.dtoList(page.getContent()))
                    .elements(page.getTotalElements())
                    .pages(page.getTotalPages())
                    .build();
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .data(new ArrayList<>())
                .build();
    }

    @Override
    public ApiResponse getUserByToken(String token) {
        Token t = this.tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));
        User user = this.userRepository.findById(t.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .data(this.userMapper.toDto(user))
                .build();
    }

    @Override
    public ApiResponse getMe(User user) {
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .data(this.userMapper.toDto(user))
                .build();
    }
}
