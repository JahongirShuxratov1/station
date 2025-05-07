package com.example.stations.repository;

import com.example.stations.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code,Long> {
    Optional<Code> findByUserIdAndDeviceId(Long id, Long deviceId);
}
