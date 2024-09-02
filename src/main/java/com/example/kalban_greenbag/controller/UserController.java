package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.user.CreateUserRequest;
import com.example.kalban_greenbag.dto.request.user.LoginRequest;
import com.example.kalban_greenbag.dto.response.JwtAuthenticationResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Create account", description = "API create new account")
    @PostMapping(value = ConstAPI.UserAPI.CREATE_ACCOUNT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtAuthenticationResponse create(@Valid @ModelAttribute CreateUserRequest createUserRequest) throws BaseException {
        log.info("Creating new account with request: {}", createUserRequest);
        return userService.create(createUserRequest);
    }


}
