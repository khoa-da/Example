package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.PAYos.CreatePaymentLinkRequestBody;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponseData;
import com.example.kalban_greenbag.entity.Order;
import com.example.kalban_greenbag.entity.OrderItem;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.repository.OrderItemRepository;
import com.example.kalban_greenbag.repository.OrderRepository;
import com.example.kalban_greenbag.service.IPayOSService;
import com.example.kalban_greenbag.service.IProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.util.*;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "PAYos Payment Controller")
public class PayOSController {

    @Autowired
    private IPayOSService payOSService;

    @Operation(summary = "Create Payment Link", description = "Create Payment Link")
    @PostMapping(value = ConstAPI.PAYosAPI.CREATE_ORDER)
    public ObjectNode createPaymentLink(@RequestBody CreatePaymentLinkRequestBody RequestBody) throws BaseException {
        return payOSService.createPaymentLink(RequestBody);
    }

    @Operation(summary = "Get Order By Code", description = "Get Order By Code")
    @GetMapping(value = ConstAPI.PAYosAPI.GET_ORDER_BY_ID + "{orderCode}")
    public ObjectNode getOrderByCode(@PathVariable("orderCode") long orderCode)  throws BaseException {
          return payOSService.getOrderById(orderCode);
    }

    @Operation(summary = "Handle payment status", description = "API Handle payment status")
    @GetMapping(value = ConstAPI.PAYosAPI.HANDLE_PAYMENT_STATUS + "{orderCode}")
    public ObjectNode handlePayment(@PathVariable("orderCode") long orderCode) throws BaseException {
        return payOSService.handlePayment(orderCode);
    }

    @Operation(summary = "Cancel Order", description = "Cancel Order")
    @PutMapping(value = ConstAPI.PAYosAPI.CANCEL_ORDER + "{orderCode}")
    public ObjectNode cancelOrder(@PathVariable("orderCode") int orderCode) throws BaseException{
        return payOSService.cancelOrder(orderCode);
    }

    @Operation(summary = "Confirm Webhook", description = "Confirm Webhook")
    @PostMapping(value = ConstAPI.PAYosAPI.CONFIRM_ORDER)
    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) throws BaseException{
        return payOSService.confirmWebhook(requestBody);
    }

    @Operation(summary = "PAYos Transfer Handler", description = "PAYos Transfer Handler")
    @PostMapping(value = ConstAPI.PAYosAPI.PAYOS_TRANSFER_HANDLER)
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws BaseException{
        return payOSService.payosTransferHandler(body);
    }
}
