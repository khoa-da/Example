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
public class UpdateOrderRequest {

    @NotNull
    private UUID orderID;

//    private UUID userID;

    @PastOrPresent(message = "Order date cannot be in the future")
    private Instant orderDate;

    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than zero")
    private BigDecimal totalAmount;

    private String orderStatus;

    @Size(max = 255, message = "Shipping address cannot exceed 255 characters")
    private String shippingAddress;

    private String status;
}
