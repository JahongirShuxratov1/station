package com.example.stations.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class StationDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StationRequest {
        private String name;
        private Double temperature;
        private Double KVT;
        private Double waterPercentage;
        private Double waterLevel;
        private String address;
        private String latitude;
        private String longitude;
        private boolean isCrash;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StationResponse {
        private Long id;
        private String name;
        private Double temperature;
        private Double KVT;
        private Double waterPercentage;
        private Double waterLevel;
        private String address;
        private String latitude;
        private String longitude;
        private boolean isCrash;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
