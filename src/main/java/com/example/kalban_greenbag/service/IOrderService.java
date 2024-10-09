package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.order.AddOrderRequest;
import com.example.kalban_greenbag.dto.request.order.UpdateOrderRequest;
import com.example.kalban_greenbag.dto.response.order.OrderResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface IOrderService extends IGenericService<OrderResponse> {
    OrderResponse create(AddOrderRequest addOrderItemRequest) throws BaseException;
    OrderResponse update(UpdateOrderRequest updateOrderItemRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
    void updateOrderTotalAmount(UUID orderId, BigDecimal totalAmount) throws BaseException;
    PagingModel<OrderResponse> getOrderByOrderCode(long orderCode, Integer page, Integer limit) throws BaseException;
    PagingModel<OrderResponse> getOrderByUserId(UUID userId, Integer page, Integer limit, String status) throws BaseException;
    List<OrderResponse> getOrderByCriteria(
            Date startDate,
            Date endDate,
            String status,
            String orderStatus) throws BaseException;
}

