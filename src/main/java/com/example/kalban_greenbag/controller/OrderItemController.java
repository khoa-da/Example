package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.base_model.AddBaseModelRequest;
import com.example.kalban_greenbag.dto.request.base_model.UpdateBaseModelRequest;
import com.example.kalban_greenbag.dto.request.order_item.AddOrderItemRequest;
import com.example.kalban_greenbag.dto.request.order_item.UpdateOrderItemRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.entity.OrderItem;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.IOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@Tag(name = "Order Item Controller")
public class OrderItemController {

    @Autowired
    private IOrderItemService orderItemService;

    @Operation(summary = "Create order item", description = "API create order item")
    @PostMapping(value = ConstAPI.OderItemAPI.CREATE_ORDER_ITEM)
    public OrderItemResponse createOrderItem(@Valid @RequestBody AddOrderItemRequest addBaseModelRequest) throws BaseException {
        return orderItemService.create(addBaseModelRequest);
    }

    @Operation(summary = "Update order item", description = "API update order item")
    @PatchMapping(value = ConstAPI.OderItemAPI.UPDATE_ORDER_ITEM)
    public OrderItemResponse updateOrderItem(@Valid @RequestBody UpdateOrderItemRequest updateOrderItemRequest) throws BaseException {
        return orderItemService.update(updateOrderItemRequest);
    }

    @Operation(summary = "Change order item status", description = "API change order item status")
    @DeleteMapping(value = ConstAPI.OderItemAPI.DELETE_ORDER_ITEM + "{id}")
    public Boolean changeOrderItemStatus(@PathVariable("id") UUID id) throws BaseException {
        return orderItemService.changeStatus(id);
    }

    @Operation(summary = "Find order item by ID", description = "API find order item by ID")
    @GetMapping(value = ConstAPI.OderItemAPI.GET_ORDER_ITEM_BY_ID + "{id}")
    public OrderItemResponse findOrderItemById(@PathVariable("id") UUID id) throws BaseException {
        return orderItemService.findById(id);
    }

    @Operation(summary = "Get all order items", description = "API get all order items with pagination")
    @GetMapping(value = ConstAPI.OderItemAPI.GET_ALL_ORDER_ITEMS)
    public PagingModel<OrderItemResponse> getAllOrderItems(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return orderItemService.getAll(page, limit);
    }

    @Operation(summary = "Get all active order items", description = "API get all active order items (status = true) with pagination")
    @GetMapping(value = ConstAPI.OderItemAPI.GET_ALL_ORDER_ITEMS_STATUS_TRUE)
    public PagingModel<OrderItemResponse> getAllActiveOrderItems(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return orderItemService.findAllByStatusTrue(page, limit);
    }



}
