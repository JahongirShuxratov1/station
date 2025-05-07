package com.example.stations.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String email;
    private String fullname;
    private String status;
    private List<String> role = new ArrayList<>();

}
