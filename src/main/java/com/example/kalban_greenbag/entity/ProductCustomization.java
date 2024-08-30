package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "ProductCustomizations")
public class ProductCustomization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductCustomizationID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID")
    private Product productID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionID")
    private CustomizationOption optionID;

    @Size(max = 100)
    @Nationalized
    @Column(name = "CustomValue", length = 100)
    private String customValue;

}