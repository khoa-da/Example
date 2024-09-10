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
public class UpdateProductRequest{
    @NotNull(message = "ProductId is required")
    private UUID id;

    private BigDecimal finalPrice;
    private String status;
}
