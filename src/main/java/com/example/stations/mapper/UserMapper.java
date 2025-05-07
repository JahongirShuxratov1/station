package com.example.stations.mapper;

import com.example.stations.dto.RegisterDto;
import com.example.stations.dto.UserDto;
import com.example.stations.entity.User;
import com.example.stations.entity.UserRole;
import com.example.stations.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleRepository roleRepository;
    public User toEntityByEmail(RegisterDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .fullname(dto.getFullName())
                .username(dto.getUsername())
                .build();
    }

    public UserDto toDto(User user) {
        List<UserRole> list = this.roleRepository.findByUserId(user.getId());

        return UserDto.builder()
                .email(user.getEmail())
                .fullname(user.getFullname())
                .status(user.getStatus().toString())
                .role(list != null && !list.isEmpty() ?
                        list.stream().map(UserRole::getName).toList() : new ArrayList<>())
                .username(user.getUsername())
                .build();
    }


    public List<UserDto> dtoList(List<User> list) {
        if (list != null && !list.isEmpty()) {
            return list.stream().map(this::toDto).toList();
        }
        return new ArrayList<>();
    }
}
