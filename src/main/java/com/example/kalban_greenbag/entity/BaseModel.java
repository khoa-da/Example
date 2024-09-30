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
@Table(name = "BaseModels")
public class BaseModel extends BaseEntity{


    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ModelName", nullable = false, length = 100)
    private String modelName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "ImageURL")
    private String imageURL;

    @Column(name = "BasePrice", precision = 10, scale = 2)
    private BigDecimal basePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID")
    private Category category;

    @OneToMany(mappedBy = "baseModelID")
    private Set<CustomizationOption> customizationOptions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "baseModelID")
    private Set<Product> products = new LinkedHashSet<>();

}