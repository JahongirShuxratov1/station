package com.example.stations.repository;

import com.example.stations.entity.Notification;
import com.example.stations.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> user(User user);

    Page<Notification> findAllByUserId(Pageable pageable, Long userId);
}
