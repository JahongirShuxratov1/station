package com.example.stations.service;

import com.example.stations.entity.Device;
import org.springframework.stereotype.Component;

@Component
public interface DeviceService {

    boolean isDeviceTrusted(Long userId, String deviceId);

    Device addTrustedDevice(Long userId, String deviceId);

    Device addTrustedDeviceByLogin(Long userId, String deviceId);

}
