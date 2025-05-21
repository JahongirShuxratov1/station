package com.example.stations.repository;

import com.example.stations.entity.FCM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FCMRepository extends JpaRepository<FCM,Long> {
     Optional<FCM>findByUserId(Long userId);
    @Query("SELECT f FROM FCM f JOIN f.user u JOIN u.role r WHERE r.name = :role")
    List<FCM> findAllByUserRole(String role);
}
