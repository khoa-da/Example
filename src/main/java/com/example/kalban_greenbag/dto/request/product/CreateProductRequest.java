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
    @Size(min = 1, max = 100000, message = "Stock must be between 1 and 100")
    private Integer stock;
    private BigDecimal finalPrice;
    private String status;

}
