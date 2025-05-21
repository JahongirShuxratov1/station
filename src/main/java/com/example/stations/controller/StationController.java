package com.example.stations.controller;

import com.example.stations.dto.ApiResponse;
import com.example.stations.dto.StationDto;
import com.example.stations.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/station")
public class StationController {
    private final StationService stationService;

    @PostMapping("/createStation")
    public ApiResponse createStation(@RequestBody StationDto.StationRequest stationRequest) {
        return this.stationService.createStation(stationRequest);
    }
    @GetMapping("/getStation")
    public ApiResponse getStation(@RequestParam Long id) {
        return this.stationService.getStation(id);
    }

    @GetMapping("/getAllStations")
    public ApiResponse getStation() {
        return this.stationService.getAll();
    }

}
