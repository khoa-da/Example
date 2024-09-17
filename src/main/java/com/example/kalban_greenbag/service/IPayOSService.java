package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.PAYos.CreatePaymentLinkRequestBody;
import com.example.kalban_greenbag.exception.BaseException;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public interface IPayOSService {
    ObjectNode createPaymentLink(CreatePaymentLinkRequestBody requestBody) throws BaseException;

    ObjectNode getOrderById(long orderId) throws BaseException;

    ObjectNode handlePayment(long orderId) throws BaseException;

    ObjectNode cancelOrder(int orderId) throws BaseException;

    ObjectNode confirmWebhook(Map<String, String> requestBody) throws BaseException;

    ObjectNode payosTransferHandler(ObjectNode body) throws BaseException;
}
