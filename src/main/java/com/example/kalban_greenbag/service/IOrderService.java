package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.order.AddOrderRequest;
import com.example.kalban_greenbag.dto.request.order.UpdateOrderRequest;
import com.example.kalban_greenbag.dto.response.order.OrderResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.exception.BaseException;

import java.math.BigDecimal;
import java.util.UUID;

public interface IOrderService extends IGenericService<OrderResponse> {
    OrderResponse create(AddOrderRequest addOrderItemRequest) throws BaseException;
    OrderResponse update(UpdateOrderRequest updateOrderItemRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
    void updateOrderTotalAmount(UUID orderId, BigDecimal totalAmount) throws BaseException;
}

