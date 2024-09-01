package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Suppliers")
public class Supplier extends BaseEntity{


    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "SupplierName", nullable = false, length = 100)
    private String supplierName;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ContactPerson", length = 100)
    private String contactPerson;

    @Size(max = 100)
    @Nationalized
    @Column(name = "Email", length = 100)
    private String email;

    @Size(max = 20)
    @Nationalized
    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Nationalized
    @Lob
    @Column(name = "Address")
    private String address;

    @OneToMany(mappedBy = "supplierID")
    private Set<PurchaseOrder> purchaseOrders = new LinkedHashSet<>();

}