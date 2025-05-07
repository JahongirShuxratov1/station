package com.example.stations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class HeadHunterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeadHunterApplication.class, args);
    }

}
