package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User userID;

    @Column(name = "OrderDate")
    private Instant orderDate;

    @Column(name = "TotalAmount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 20)
    @Nationalized
    @Column(name = "Status", length = 20)
    private String status;

    @Nationalized
    @Lob
    @Column(name = "ShippingAddress")
    private String shippingAddress;

    @OneToMany(mappedBy = "orderID")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

}