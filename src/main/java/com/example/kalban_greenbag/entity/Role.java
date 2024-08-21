//package com.example.kalban_greenbag.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Table;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "role_tbl")
//public class Role extends BaseEntity {
//
//    @NotBlank(message = "This field must not be blank")
//    @NotNull(message = "This field must not be null")
//    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
//    @Column(name = "name")
//    private String name;
//
//    @NotBlank(message = "This field must not be blank")
//    @NotNull(message = "This field must not be null")
//    @Size(max = 200, message = "Description must be less than or equal to 200 characters")
//    @Column(name ="description")
//    private String description;
//
//
//}