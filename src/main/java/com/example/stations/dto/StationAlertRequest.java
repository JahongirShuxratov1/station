package com.example.stations.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class StationAlertRequest {
    private Long stationId;
    private String causeOfCrash;
}
