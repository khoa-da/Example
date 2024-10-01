package com.example.kalban_greenbag.dto.request.order;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrderRequest {

    @NotNull(message = "UserID is required")
    private UUID userID;

    // @NotNull(message = "Order date is required")
    // @PastOrPresent(message = "Order date cannot be in the future")
    // private Instant orderDate;

    @NotNull
    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be greater than or equal to zero")
    private BigDecimal totalAmount;

    @NotNull
    @NotBlank(message = "Order status is required")
    private String orderStatus;

    @NotBlank(message = "Shipping address is required")
    @Size(max = 255, message = "Shipping address cannot exceed 255 characters")
    private String shippingAddress;

}
