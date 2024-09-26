package com.example.kalban_greenbag.dto.request.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotNull(message = "BaseModelId is required")
    private UUID baseModelID;
    @NotNull(message = "ProductName is required")
    private String productName;
    @NotNull(message = "Stock is required")
    private Integer stock;
    private BigDecimal finalPrice;
    private String status;

}
