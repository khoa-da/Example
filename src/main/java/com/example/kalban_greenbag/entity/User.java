package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "Password", nullable = false)
    private String password;

    @Size(max = 100)
    @Nationalized
    @Column(name = "FullName", length = 100)
    private String fullName;

    @Nationalized
    @Lob
    @Column(name = "Address")
    private String address;

    @Size(max = 20)
    @Nationalized
    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "CreatedAt")
    private Instant createdAt;

    @Column(name = "UpdatedAt")
    private Instant updatedAt;

    @OneToMany(mappedBy = "userID")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userID")
    private Set<Review> reviews = new LinkedHashSet<>();

}