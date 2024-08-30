package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InventoryID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaterialID")
    private Material materialID;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "LastUpdated")
    private Instant lastUpdated;

}