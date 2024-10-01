package com.example.kalban_greenbag.dto.response.order;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.entity.OrderItem;
import com.example.kalban_greenbag.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse extends BaseResponse implements Serializable {
    private UUID userId;
    private Instant orderDate;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String orderStatus;
    private String orderCode;
    private Set<OrderItemResponse> orderItems;
}
