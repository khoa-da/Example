package com.example.kalban_greenbag.dto.response.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusTotalResponse {
    private String date;
    private BigDecimal pendingTotal;
    private int pendingCount;
    private BigDecimal completedTotal;
    private int completedCount;
}
