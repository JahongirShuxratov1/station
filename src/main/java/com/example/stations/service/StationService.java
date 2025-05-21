package com.example.stations.service;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.StationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface StationService {
    ApiResponse getAll();

    ApiResponse createStation(StationDto.StationRequest stationRequest);

    ApiResponse getStation(Long id);
}
