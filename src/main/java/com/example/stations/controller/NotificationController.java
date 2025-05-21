package com.example.stations.controller;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.FCMDto;
import com.example.stations.dto.StationAlertRequest;
import com.example.stations.dto.StationDto;
import com.example.stations.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/createToken")
    public ApiResponse createToken(@RequestBody FCMDto request) {
        return this.notificationService.createToken(request);
    }



    @PostMapping("/createNotification")
    public ApiResponse createNotification(@RequestParam String text,
                                          @RequestParam Long userId) {
         this.notificationService.createNotification(text,userId);
         return ApiResponse.builder().status(HttpStatus.OK).message("SUCCESS").build();
    }
    @PostMapping("/createNotificationForTezkorUser")
    public ApiResponse createNotification(@RequestBody StationAlertRequest stationAlertRequest) {
         this.notificationService.createNotificationForTezkorUser(stationAlertRequest);
         return ApiResponse.builder().status(HttpStatus.OK).message("SUCCESS").build();
    }

    @PostMapping("/createNotificationForAllUsers")
    public ApiResponse createNotification(@RequestParam String text) {
         this.notificationService.createNotificationForAllUsers(text);
         return ApiResponse.builder().status(HttpStatus.OK).message("SUCCESS").build();
    }

    @GetMapping("/getAllNotificationByUserId")
    public ApiResponse getAllNotificationByUserId(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                                                  @RequestParam Long userId) {
        return this.notificationService.getAllNotificationByUserId(PageRequest.of(page,size),userId);
    }


}
