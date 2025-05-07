package com.example.stations.controller;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.RoleDto;
import com.example.stations.service.RoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ApiResponse create(@RequestBody @NonNull RoleDto.CreateRole dto) {
        return this.roleService.create(dto);
    }

    @GetMapping("/getAll")
    public ApiResponse getAll() {
        return this.roleService.getAll();
    }
}
