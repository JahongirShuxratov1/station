package com.example.stations.service.impl;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.StationDto;
import com.example.stations.entity.Station;
import com.example.stations.mapper.StationMapper;
import com.example.stations.repository.StationRepository;
import com.example.stations.service.StationService;
import com.example.stations.service.StationSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final StationMapper stationMapper;

    @Override
    public ApiResponse getAll(Pageable pageable) {
        Page<Station> stationPage = stationRepository.findAll(pageable);
        return ApiResponse.builder().
                status(HttpStatus.OK)
                .message("SUCCESS")
                .data(stationPage)
                .build();
    }

    @Override
    public ApiResponse createStation(StationDto.StationRequest request) {
        Station station = this.stationMapper.toEntity(request);
        station = stationRepository.save(station);
        StationDto.StationResponse response = this.stationMapper.toDto(station);
        messagingTemplate.convertAndSend("/topic/stations", response);
        return ApiResponse.builder().
                status(HttpStatus.OK)
                .message("SUCCESS")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse getStation(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));

        StationDto.StationResponse response = this.stationMapper.toDto(station);
        return ApiResponse.builder().
                status(HttpStatus.OK)
                .message("SUCCESS")
                .data(response)
                .build();    }
}
