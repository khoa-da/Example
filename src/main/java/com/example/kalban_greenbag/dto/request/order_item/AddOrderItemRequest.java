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
public class AddOrderItemRequest {

    @NotNull(message = "OrderID is required")
    private UUID orderID;

    @NotNull(message = "ProductID is required")
    private UUID productID;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private Integer quantity;

    @NotNull(message = "UnitPrice is required")
    @Min(value = 0, message = "UnitPrice must be greater than or equal to 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Status is required")
    private String status;
}
