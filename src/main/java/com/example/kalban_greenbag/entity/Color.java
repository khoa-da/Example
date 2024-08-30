package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "Colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ColorID", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Nationalized
    @Column(name = "ColorName", nullable = false, length = 30)
    private String colorName;

    @Size(max = 7)
    @Nationalized
    @Column(name = "ColorCode", length = 7)
    private String colorCode;

}