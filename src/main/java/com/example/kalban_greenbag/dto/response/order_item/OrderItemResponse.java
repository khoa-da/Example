package com.example.kalban_greenbag.dto.response.order_item;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.entity.Order;
import com.example.kalban_greenbag.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse extends BaseResponse implements Serializable {
    private UUID orderID;
    private UUID productID;
    private Integer quantity;
    private BigDecimal unitPrice;
}
