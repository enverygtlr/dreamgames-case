package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSaveRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }
}
