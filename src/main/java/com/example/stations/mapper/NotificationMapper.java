package com.example.stations.mapper;

import com.example.stations.dto.NotificationDto;
import com.example.stations.entity.User;
import com.example.stations.entity.Notification;
import com.example.stations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationMapper {

    private final UserRepository userRepository;

    public Notification toEntity(NotificationDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        return Notification.builder()
                .text(dto.getText())
                .user(user)
                .build();
    }

    public NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .text(notification.getText())
                .userId(notification.getUser().getId())
                .build();
    }

    public List<NotificationDto> toDtoList(List<Notification> notifications) {
        if (notifications != null && !notifications.isEmpty()) {
            return notifications.stream()
                    .map(this::toDto)
                    .toList();
        }
        return new ArrayList<>();
    }
}
