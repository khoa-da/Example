package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BaseModelID")
    private BaseModel baseModelID;

    @Column(name = "FinalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "CreatedAt")
    private Instant createdAt;

    @OneToMany(mappedBy = "productID")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productID")
    private Set<ProductCustomization> productCustomizations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productID")
    private Set<Review> reviews = new LinkedHashSet<>();

}