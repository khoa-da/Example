package com.example.kalban_greenbag.dto.response.product_customization;

import java.io.Serializable;
import java.math.BigDecimal;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.entity.CustomizationOption;
import com.example.kalban_greenbag.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCustomizationResponse extends BaseResponse{
    private UUID optionID;
    private String imageURL;
    private String customValue;
    private UUID userId;
    private BigDecimal totalPrice;
    private ProductResponse productID;
    private String reason;
}
