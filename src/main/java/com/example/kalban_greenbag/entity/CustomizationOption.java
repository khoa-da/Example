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
@Table(name = "CustomizationOptions")
public class CustomizationOption extends BaseEntity{


    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "OptionName", nullable = false, length = 50)
    private String optionName;

    @Size(max = 20)
    @NotNull
    @Nationalized
    @Column(name = "OptionType", nullable = false, length = 20)
    private String optionType;

    @Column(name = "AdditionalPrice", precision = 10, scale = 2)
    private BigDecimal additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BaseModelID")
    private BaseModel baseModelID;

    @OneToMany(mappedBy = "optionID")
    private Set<ProductCustomization> productCustomizations = new LinkedHashSet<>();

}