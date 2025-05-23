package com.example.stations.repository;

import com.example.stations.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long> {
    Optional<Device> findByUserIdAndDeviceName(Long userId, String deviceId);

}
