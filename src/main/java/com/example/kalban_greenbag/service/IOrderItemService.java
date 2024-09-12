package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.order_item.AddOrderItemRequest;
import com.example.kalban_greenbag.dto.request.order_item.UpdateOrderItemRequest;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.exception.BaseException;

import java.util.UUID;

public interface IOrderItemService extends IGenericService<OrderItemResponse> {
    OrderItemResponse create(AddOrderItemRequest addOrderItemRequest) throws BaseException;
    OrderItemResponse update(UpdateOrderItemRequest updateOrderItemRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
}
