package com.example.stations.service;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.RoleDto;
import lombok.NonNull;

public interface RoleService {
    ApiResponse create(RoleDto.@NonNull CreateRole dto);

    ApiResponse getAll();
}
