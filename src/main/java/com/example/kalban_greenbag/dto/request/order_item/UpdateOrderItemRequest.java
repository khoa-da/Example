package com.example.kalban_greenbag.dto.request.order_item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderItemRequest {
    @NotNull(message = "OrderItemID is required")
    private UUID orderItemID;

    private UUID orderID;

    private UUID productID;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;

    @Min(value = 0, message = "UnitPrice must be greater than or equal to 0")
    private BigDecimal unitPrice;

    private String status;
}
