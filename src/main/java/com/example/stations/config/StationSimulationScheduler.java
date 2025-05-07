package com.example.stations.config;

import com.example.stations.service.StationService;
import com.example.stations.service.StationSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class StationSimulationScheduler {

    @Qualifier("stationSimulationService")
    private final StationSimulationService simulatorService;

    @Scheduled(fixedRate = 10000)
    public void runSimulation() {
        simulatorService.simulateData();
    }
}
