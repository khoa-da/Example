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
@Table(name = "PurchaseOrders")
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PurchaseOrderID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SupplierID")
    private Supplier supplierID;

    @Column(name = "OrderDate")
    private Instant orderDate;

    @Column(name = "TotalAmount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 20)
    @Nationalized
    @Column(name = "Status", length = 20)
    private String status;

    @OneToMany(mappedBy = "purchaseOrderID")
    private Set<PurchaseOrderItem> purchaseOrderItems = new LinkedHashSet<>();

}