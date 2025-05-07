package com.example.stations.service.impl;

import ch.qos.logback.core.joran.action.AppenderRefAction;
import com.example.stations.entity.Station;
import com.example.stations.mapper.StationMapper;
import com.example.stations.repository.StationRepository;
import com.example.stations.service.NotificationService;
import com.example.stations.service.StationSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StationSimulationServiceImpl implements StationSimulationService {

    private final StationRepository stationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final StationMapper stationMapper;

    private static final Random RANDOM = new Random();
    private final NotificationService notificationService;

    public void simulateData() {
        List<Station> stations = stationRepository.findAll();

        for (Station station : stations) {
            double temperature = getRandom(100, 800);
            double kvt = getRandom(100, 400);
            double waterPercent = getRandom(5, 18);
            double waterLevel = getRandom(3.5, 10);

            if (RANDOM.nextInt(20) == 0) {
                temperature = getRandom(1000, 1300);
            }
            if (RANDOM.nextInt(30) == 0) {
                kvt = getRandom(520, 700);
            }
            if (RANDOM.nextInt(40) == 0) {
                waterPercent = getRandom(25, 35);
            }
            if (RANDOM.nextInt(25) == 0) {
                waterLevel = getRandom(1.5, 2.8);
            }

            station.setTemperature(temperature);
            station.setKVT(kvt);
            station.setWaterPercentage(waterPercent);
            station.setWaterLevel(waterLevel);

            boolean isCrash = temperature > 1000 || kvt > 500 || waterPercent > 20 || waterLevel < 3;

            if (isCrash) {
                notificationService.createNotificationForAllUsers("Critical issue detected!:"+ station.getName()+","+station.getAddress());
            }

            station.setCrash(isCrash);
            station.setUpdatedAt(LocalDateTime.now());

            stationRepository.save(station);

            messagingTemplate.convertAndSend("/topic/stations", this.stationMapper.toDto(station));
        }

    }
    private double getRandom(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble();
    }
}