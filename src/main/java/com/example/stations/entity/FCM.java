package com.example.stations.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fcm")
public class FCM extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String fcm_token;
}
