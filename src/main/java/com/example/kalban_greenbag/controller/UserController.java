package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.user.LoginRequest;
import com.example.kalban_greenbag.dto.response.JwtAuthenticationResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "User Controller")
public class UserController {
    @Autowired
private IUserService userService;

    @Operation(summary = "Login", description = "API login ")
    @PostMapping(value = ConstAPI.AuthenticationAPI.LOGIN_WITH_PASSWORD_USERNAME)
    public JwtAuthenticationResponse login(@RequestBody LoginRequest request) throws BaseException {
        log.info("Creating new account with request: {}", request);
        return userService.login(request);
    }
}
