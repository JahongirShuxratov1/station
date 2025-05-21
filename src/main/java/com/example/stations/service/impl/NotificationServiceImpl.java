package com.example.stations.service.impl;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.FCMDto;
import com.example.stations.dto.StationAlertRequest;
import com.example.stations.entity.FCM;
import com.example.stations.entity.Station;
import com.example.stations.entity.User;
import com.example.stations.exceptions.BadRequestException;
import com.example.stations.exceptions.ResourceNotFoundException;
import com.example.stations.repository.FCMRepository;
import com.example.stations.repository.NotificationRepository;
import com.example.stations.repository.StationRepository;
import com.example.stations.repository.UserRepository;
import com.example.stations.service.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final FCMRepository fcmRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final StationRepository stationRepository;

    @Override
    public ApiResponse createToken(FCMDto request) {
        Optional<FCM> existing = fcmRepository.findByUserId(request.getUserId());
        User user = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        FCM token = existing
                .map(t -> {
                    t.setFcm_token(request.getFcm_token());
                    return t;
                })
                .orElseGet(() -> FCM.builder()
                        .user(user)
                        .fcm_token(request.getFcm_token())
                        .build()
                );

        fcmRepository.save(token);

        return ApiResponse.builder()
                .message("SUCCESS")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @Async
    public CompletableFuture<ApiResponse> createNotification(String text, Long userId) {
        FCM token = fcmRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found for user: " + userId));
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Message message = Message.builder()
                .setToken(token.getFcm_token())
                .setNotification(Notification.builder()
                        .setTitle("New Notification")
                        .setBody(text)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            com.example.stations.entity.Notification notification = new com.example.stations.entity.Notification(user, text);
            this.notificationRepository.save(notification);
            return CompletableFuture.completedFuture(ApiResponse.builder()
                    .message("SUCCESS")
                    .status(HttpStatus.OK)
                    .data(response)
                    .build());
        } catch (FirebaseMessagingException e) {
            return CompletableFuture.completedFuture(ApiResponse.builder()
                    .message("Error: " + e)
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }

    @Override
    @Async
    public CompletableFuture<ApiResponse> createNotificationForAllUsers(String text) {
        List<FCM> tokens = fcmRepository.findAll();


        for (FCM token : tokens) {
            User user = userRepository.findById(token.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Message message = Message.builder()
                    .setToken(token.getFcm_token())
                    .setNotification(Notification.builder()
                            .setTitle("New Notification")
                            .setBody(text)
                            .build())
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                com.example.stations.entity.Notification notification = new com.example.stations.entity.Notification(user, text);
                this.notificationRepository.save(notification);
            } catch (FirebaseMessagingException e) {
                throw new BadRequestException(e);
            }

            com.example.stations.entity.Notification notification =
                    new com.example.stations.entity.Notification(user, text);
            this.notificationRepository.save(notification);


        }


        return CompletableFuture.completedFuture(ApiResponse.builder()
                .message("SUCCESS")
                .status(HttpStatus.OK)
                .build());
    }


    @Override
    public ApiResponse getAllNotificationByUserId(Pageable pageable, Long userId) {
        Page<com.example.stations.entity.Notification> notifications = this.notificationRepository.findAllByUserId(pageable, userId);
        return ApiResponse.builder()
                .message("SUCCESS")
                .status(HttpStatus.OK)
                .data(notifications)
                .build();
    }

    @Override
    public CompletableFuture<ApiResponse> createNotificationForTezkorUser(StationAlertRequest stationAlertRequest) {
        List<FCM> tokens = fcmRepository.findAllByUserRole("TEZKOR");
        Station station = this.stationRepository.findById(stationAlertRequest.getStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));

        for (FCM token : tokens) {
            User user = userRepository.findById(token.getUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Message message = Message.builder()
                    .setToken(token.getFcm_token())
                    .setNotification(Notification.builder()
                            .setTitle("New Notification")
                            .setBody(stationAlertRequest.getCauseOfCrash())
                            .build())
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                com.example.stations.entity.Notification notification = new com.example.stations.entity.Notification(user, stationAlertRequest.getCauseOfCrash());
                this.notificationRepository.save(notification);
            } catch (FirebaseMessagingException e) {
                throw new BadRequestException(e);
            }

            com.example.stations.entity.Notification notification =
                    new com.example.stations.entity.Notification(user, stationAlertRequest.getCauseOfCrash());
            this.notificationRepository.save(notification);


        }
        return CompletableFuture.completedFuture(ApiResponse.builder()
                .message("SUCCESS")
                .status(HttpStatus.OK)
                .build());


    }

}
