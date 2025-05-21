package com.example.stations.service;

import com.example.stations.dto.ApiResponse;
import com.example.stations.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    ApiResponse addRoleToUser(Long userId, Long roleId);

    ApiResponse deleteRoleFromUser(Long userId, Long roleId);

    ApiResponse getUserById(Long id);

    ApiResponse updateUserNameAndFullName(Long userId, String username, String fullname);

    ApiResponse getAllActiveUser(Pageable pageable);

    ApiResponse getUserByToken(String token);

    ApiResponse getMe(User user);
}
