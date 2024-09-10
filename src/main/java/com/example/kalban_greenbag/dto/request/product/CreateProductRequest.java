package com.example.kalban_greenbag.dto.request.product;

import jakarta.validation.constraints.NotNull;
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
    private BigDecimal finalPrice;
    private String status;

}
