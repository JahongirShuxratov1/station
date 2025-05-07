package com.example.stations.repository;

import com.example.stations.entity.FCM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FCMRepository extends JpaRepository<FCM,Long> {
     Optional<FCM>findByUserId(Long userId);
}
