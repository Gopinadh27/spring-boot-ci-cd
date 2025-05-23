package com.gnr.ci.cd.spring_boot_ci_cd.controller;

import com.gnr.ci.cd.spring_boot_ci_cd.entity.User;
import com.gnr.ci.cd.spring_boot_ci_cd.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(
            method = "POST",
            summary = "User registration"
    )
    @PostMapping
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public User findByEmail(@PathVariable(required = true) String email) {
        return userService.findByEmail(email);
    }
}
