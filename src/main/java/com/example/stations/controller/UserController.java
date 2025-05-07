package com.example.stations.controller;

import com.example.stations.dto.ApiResponse;
import com.example.stations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class UserController {

    private final UserService userService;

    @PostMapping("/addRoleToUser")
    public ApiResponse addRoleToUser(@RequestParam("userId") Long userId,
                                     @RequestParam("roleId") Long roleId) {
        return this.userService.addRoleToUser(userId, roleId);
    }
    @DeleteMapping("/deleteRoleFromUser")
    public ApiResponse deleteRoleFromUser(@RequestParam("userId") Long userId,
                                          @RequestParam("roleId") Long roleId) {
        return this.userService.deleteRoleFromUser(userId, roleId);
    }

    @GetMapping("/getUserById")
    public ApiResponse getUserById(@RequestParam("id") Long id) {
        return this.userService.getUserById(id);
    }


    @PutMapping("/updateUserNameAndFullName")
    public ApiResponse updateUserNameAndFullName(@RequestParam("userId") Long userId,
                                                 @RequestParam("username") String username,
                                                 @RequestParam("fullname") String fullname) {
        return this.userService.updateUserNameAndFullName(userId, username, fullname);
    }


    @GetMapping("/getAllActiveUser")
    public ApiResponse getAllActiveUser(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        return this.userService.getAllActiveUser(PageRequest.of(page, size));
    }


    @GetMapping("/getUserByToken")
    public ApiResponse getUserByToken(@RequestParam("token") String token) {
        return this.userService.getUserByToken(token);
    }
}
