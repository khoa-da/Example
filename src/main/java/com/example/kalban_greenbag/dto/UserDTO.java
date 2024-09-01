package com.example.kalban_greenbag.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.example.kalban_greenbag.entity.User}
 */
@Value
@Data
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 50)
    String username;
    @NotNull
    @Size(max = 100)
    String email;
    @NotNull
    @Size(max = 255)
    String password;
    @Size(max = 100)
    String fullName;
    String address;
    @Size(max = 20)
    String phoneNumber;
    Instant createdAt;
    Instant updatedAt;
}