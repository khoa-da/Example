package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Materials")
public class Material extends BaseEntity{

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "MaterialName", nullable = false, length = 50)
    private String materialName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "StockQuantity")
    private Integer stockQuantity;

    @Column(name = "UnitPrice", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @OneToMany(mappedBy = "materialID")
    private Set<Inventory> inventories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "materialID")
    private Set<PurchaseOrderItem> purchaseOrderItems = new LinkedHashSet<>();

}