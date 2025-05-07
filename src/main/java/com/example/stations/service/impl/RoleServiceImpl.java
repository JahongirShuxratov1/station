package com.example.stations.service.impl;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.RoleDto;
import com.example.stations.entity.UserRole;
import com.example.stations.mapper.RoleMapper;
import com.example.stations.repository.RoleRepository;
import com.example.stations.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class  RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public ApiResponse create(RoleDto.CreateRole dto) {
        UserRole role = this.roleMapper.toEntity(dto);
        role.setCreatedAt(LocalDateTime.now());
        this.roleRepository.save(role);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("ROLE CREATED")
                .build();
    }

    @Override
    public ApiResponse getAll() {
        List<UserRole> list = this.roleRepository.findAll();
        if (!list.isEmpty()) {
            return ApiResponse.builder()
                    .status(HttpStatus.OK)
                    .data(this.roleMapper.dtoList(list))
                    .message("SUCCESS")
                    .build();
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("SUCCESS")
                .data(new ArrayList<>())
                .build();
    }
}
