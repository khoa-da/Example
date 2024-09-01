package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.user.LoginRequest;
import com.example.kalban_greenbag.dto.response.JwtAuthenticationResponse;
import com.example.kalban_greenbag.dto.response.user.UserResponse;
import com.example.kalban_greenbag.exception.BaseException;

public interface IUserService extends IGenericService<UserResponse> {
    JwtAuthenticationResponse login(LoginRequest loginRequest) throws BaseException;
}
