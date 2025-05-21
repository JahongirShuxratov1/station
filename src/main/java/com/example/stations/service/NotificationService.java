package com.example.stations.service;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.FCMDto;
import com.example.stations.dto.StationAlertRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public interface NotificationService {
    ApiResponse createToken(FCMDto request);

    CompletableFuture<ApiResponse> createNotification(String text, Long userId);

    CompletableFuture<ApiResponse> createNotificationForAllUsers(String text);

    ApiResponse getAllNotificationByUserId(Pageable pageable, Long userId);

    CompletableFuture<ApiResponse> createNotificationForTezkorUser(StationAlertRequest stationAlertRequest);
}
