package com.example.stations.mapper;

import com.example.stations.dto.StationDto;
import com.example.stations.dto.StationDto.StationRequest;
import com.example.stations.dto.StationDto.StationResponse;
import com.example.stations.entity.Station;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class StationMapper {

    public Station toEntity(StationDto.StationRequest request) {
        return Station.builder()
                .name(request.getName())
                .temperature(request.getTemperature())
                .KVT(request.getKVT())
                .WaterPercentage(request.getWaterPercentage())
                .WaterLevel(request.getWaterLevel())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isCrash(request.isCrash())
                .build();
    }

    public StationResponse toDto(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .temperature(station.getTemperature())
                .KVT(station.getKVT())
                .waterPercentage(station.getWaterPercentage())
                .waterLevel(station.getWaterLevel())
                .address(station.getAddress())
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .isCrash(station.isCrash())
                .createdAt(station.getCreatedAt())
                .updatedAt(station.getUpdatedAt())
                .build();
    }
    public List<StationDto.StationResponse> dtoList(List<Station> list) {
        if (list != null && !list.isEmpty()) {
            return list.stream().map(this::toDto).toList();
        }
        return new ArrayList<>();
    }
}
