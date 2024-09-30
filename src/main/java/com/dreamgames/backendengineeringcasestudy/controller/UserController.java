package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.domain.request.UserSaveRequest;
import com.dreamgames.backendengineeringcasestudy.domain.request.UserUpdateLevelRequest;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserResponse;
import com.dreamgames.backendengineeringcasestudy.domain.response.UserUpdateLevelResponse;
import com.dreamgames.backendengineeringcasestudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserSaveRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @PutMapping("/update-level")
    public ResponseEntity<UserUpdateLevelResponse> updateProgress(@RequestBody UserUpdateLevelRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }


}
