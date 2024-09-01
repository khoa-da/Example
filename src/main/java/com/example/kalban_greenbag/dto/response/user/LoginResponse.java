package com.example.kalban_greenbag.dto.response.user;

import com.example.kalban_greenbag.entity.Role;
import lombok.Data;

import java.time.Instant;

@Data
public class LoginResponse {
    private int id;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private Role role;
    private String token;
    private String refreshToken;
    private Instant expiredTime;
}
