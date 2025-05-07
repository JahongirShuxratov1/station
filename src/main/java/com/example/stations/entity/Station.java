package com.example.stations.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "station")
public class Station extends BaseEntity{
    private String name;
    private Double temperature;
    private Double KVT;
    private Double WaterPercentage;
    private Double WaterLevel;
    private String address;
    private String latitude;
    private String longitude;
    private boolean isCrash=false;
}
