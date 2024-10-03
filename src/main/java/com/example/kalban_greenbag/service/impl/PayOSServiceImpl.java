package com.example.kalban_greenbag.service.impl;

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
import org.modelmapper.ModelMapper;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.*;

import java.util.*;

@Service
public class PayOSServiceImpl implements IPayOSService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final PayOS payOS;

    public PayOSServiceImpl(PayOS payOS) {
        super();
        this.payOS = payOS;
    }

    @Override
    public ObjectNode createPaymentLink(CreatePaymentLinkRequestBody requestBody) throws BaseException {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode response = objectMapper.createObjectNode();
            int totalPrice;
            String returnUrl, cancelUrl;
            List<OrderItemResponseData> orderItems = new ArrayList<>();
            try {
                final UUID orderId = requestBody.getOrderId();
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Order.ORDER_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));
                List<OrderItem> orderItemList = orderItemRepository.findByOrderID_Id(orderId);
                for (OrderItem orderItem : orderItemList) {
                    OrderItemResponseData orderItemResponseData = OrderItemResponseData.builder()
                            .name(orderItem.getProductID().getProductName())
                            .quantity(orderItem.getQuantity())
                            .price(orderItem.getUnitPrice().intValue())
                            .build();
                    orderItems.add(orderItemResponseData);
                }
                totalPrice = order.getTotalAmount().intValue();
                final String description = requestBody.getDescription();
                returnUrl = requestBody.getReturnUrl();
                cancelUrl = requestBody.getCancelUrl();
                List<ItemData> items = modelMapper.map(orderItems, List.class);
                String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
                long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
    //            ItemData item = ItemData.builder().name(productName).price(price).quantity(1).build();
                order.setOrderCode(orderCode);
                returnUrl += "?orderCode=" + orderCode;
                cancelUrl += "?orderCode=" + orderCode;
                orderRepository.save(order);
                PaymentData paymentData = PaymentData.builder().orderCode(orderCode).description(description).amount(totalPrice)
                        .items(items).returnUrl(returnUrl).cancelUrl(cancelUrl).build();
                CheckoutResponseData data = payOS.createPaymentLink(paymentData);
                response.put("error", 0);
                response.put("message", "success");
                response.set("data", objectMapper.valueToTree(data));
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                response.put("error", -1);
                response.put("message", "fail");
                response.set("data", null);
                return response;
            }
    }

    @Override
    public ObjectNode getOrderById(long orderId) throws BaseException {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode response = objectMapper.createObjectNode();
            try {
                PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);
                response.set("data", objectMapper.valueToTree(order));
                response.put("error", 0);
                response.put("message", "ok");
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                response.put("error", -1);
                response.put("message", e.getMessage());
                response.set("data", null);
                return response;
            }
    }


    @Override
    public ObjectNode handlePayment(long orderId) throws BaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);
            Order orderToUpdate = orderRepository.findByOrderCode(orderId);

            if (order.getStatus().equals(ConstStatus.PayOsStatus.PAYOS_STATUS_PAID)) {
                orderToUpdate.setOrderStatus(ConstStatus.OrderStatus.ORDER_STATUS_PAID);
                for (OrderItem orderItem : orderToUpdate.getOrderItems()) {
                    checkAndReduceProductStock(orderItem);
                }
            } else if (order.getStatus().equals(ConstStatus.PayOsStatus.PAYOS_STATUS_CANCELLED)) {
                orderToUpdate.setOrderStatus(ConstStatus.OrderStatus.ORDER_STATUS_CANCELLED);
            } else {
                orderToUpdate.setOrderStatus(ConstStatus.OrderStatus.ORDER_STATUS_PENDING);
            }

            orderRepository.save(orderToUpdate);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    ConstError.PayOS.HANDLE_PAYMENT_FAILED,
                    e.getMessage()
            );
        }
    }


    @Override
    public ObjectNode cancelOrder(int orderId) throws BaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            PaymentLinkData order = payOS.cancelPaymentLink(orderId, null);
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    ConstError.PayOS.CANCELED_ORDER_FAILED,
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

    @Override
    public ObjectNode confirmWebhook(Map<String, String> requestBody) throws BaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    ConstError.PayOS.WEBHOOK_PROCESS_FAILED,
                    "Failed to process Webhook: " + e.getMessage()
            );
        }
    }

    @Override
    public ObjectNode payosTransferHandler(ObjectNode body) throws BaseException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);
            WebhookData data = payOS.verifyPaymentWebhookData(webhookBody);
            response.put("error", 0);
            response.put("message", "Webhook delivered successfully");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (JsonProcessingException e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    ConstError.PayOS.INVALID_WEBHOOK_DATA,
                    "Invalid Webhook data: " + e.getMessage()
            );

        } catch (Exception e) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    ConstError.PayOS.WEBHOOK_PROCESS_FAILED,
                    "Failed to process Webhook: " + e.getMessage()
            );
        }
    }

    private void checkAndReduceProductStock(OrderItem orderItem) throws BaseException {
        boolean success = productService.reduceProductStock(orderItem.getProductID().getId(), orderItem.getQuantity());

        if (!success) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    ConstError.Product.PRODUCT_STOCK_NOT_ENOUGH,
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

}
