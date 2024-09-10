package com.example.kalban_greenbag.dto.response.customization_option;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.entity.BaseModel;
import com.example.kalban_greenbag.entity.ProductCustomization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomizationOptionResponse extends BaseResponse implements Serializable {

    private String optionName;
    private String optionType;
    private BigDecimal additionalPrice;
//    private BaseModel baseModelID;
    private Set<ProductCustomization> productCustomizations;
}
