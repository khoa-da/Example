package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ProductCustomizations")
public class ProductCustomization extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID")
    private Product productID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionID")
    private CustomizationOption optionID;

    @Column(name = "ImageURL")
    private String imageURL;

    @Column(name = "UserId")
    private UUID userId;

    @Nationalized
    @Column(name = "CustomValue", columnDefinition = "NVARCHAR(MAX)")  // Sử dụng NVARCHAR(MAX) cho các DB hỗ trợ
    private String customValue;
}
