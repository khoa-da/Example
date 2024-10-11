package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
//import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.order_item.AddOrderItemRequest;
import com.example.kalban_greenbag.dto.request.order_item.UpdateOrderItemRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.customization_option.CustomizationOptionResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.entity.*;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.OrderItemRepository;
import com.example.kalban_greenbag.repository.OrderRepository;
import com.example.kalban_greenbag.repository.ProductRepository;
import com.example.kalban_greenbag.service.IOrderItemService;
import com.example.kalban_greenbag.service.IOrderService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import com.example.kalban_greenbag.utils.ValidateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements IOrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IOrderService orderService;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    public int totalItem() {
        return (int) orderItemRepository.count();
    }

    @Override
    public OrderItemResponse findById(UUID id) throws BaseException {
        try {
//            String redisKey = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER_ITEM + id.toString();
//
//            OrderItemResponse orderItemResponse = (OrderItemResponse) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER_ITEM, redisKey);
//            if (orderItemResponse != null) {
//                return orderItemResponse;
//            }
            OrderItemResponse orderItemResponse = null;
            OrderItem orderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.OrderItem.ORDER_ITEM_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            orderItemResponse = modelMapper.map(orderItem, OrderItemResponse.class);
//            redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER_ITEM, redisKey, orderItemResponse);

            return orderItemResponse;
        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }


    @Override
    public PagingModel<OrderItemResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            PagingModel<OrderItemResponse> result = new PagingModel<>();
            result.setPage(page);

            Pageable pageable = PageRequest.of(page - 1, limit);

            // Fetch Order Items and map to response objects
            List<OrderItem> orderItems = orderItemRepository.findAllByOrderByCreatedDate(pageable);
            List<OrderItemResponse> orderItemResponseList = orderItems.stream()
                    .map(orderItem -> {
                        OrderItemResponse response = modelMapper.map(orderItem, OrderItemResponse.class);
                        ProductResponse productResponse = modelMapper.map(orderItem.getProductID(), ProductResponse.class);
                        response.setProduct(productResponse);
                        response.setOrderID(orderItem.getOrderID().getId());
                        return response;
                    })
                    .toList();

            result.setListResult(orderItemResponseList);

            // Set pagination data
            long totalItems = orderItemRepository.count();
            result.setTotalPage((int) Math.ceil((double) totalItems / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }



    @Override
    public PagingModel<OrderItemResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            PagingModel<OrderItemResponse> result = new PagingModel<>();
            result.setPage(page);

            Pageable pageable = PageRequest.of(page - 1, limit);

            // Fetch Order Items with active status and map to response objects
            List<OrderItem> orderItems = orderItemRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            List<OrderItemResponse> orderItemResponseList = orderItems.stream()
                    .map(orderItem -> {
                        OrderItemResponse response = modelMapper.map(orderItem, OrderItemResponse.class);
                        ProductResponse productResponse = modelMapper.map(orderItem.getProductID(), ProductResponse.class);
                        response.setProduct(productResponse);
                        response.setOrderID(orderItem.getOrderID().getId());
                        return response;
                    })
                    .toList();

            result.setListResult(orderItemResponseList);

            // Set pagination data
            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


    @Override
    public OrderItemResponse create(AddOrderItemRequest addOrderItemRequest) throws BaseException {
        try {
            Integer quantity = null;
            BigDecimal unitPrice, totalAmountOfOrder = null;
            String username = SecurityUtil.getCurrentUsername();

            Order order = orderRepository.findById(addOrderItemRequest.getOrderID())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.Order.ORDER_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            Product product = productRepository.findById(addOrderItemRequest.getProductID())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.Product.PRODUCT_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            quantity = addOrderItemRequest.getQuantity();
            unitPrice = product.getFinalPrice().multiply(BigDecimal.valueOf(quantity));
            totalAmountOfOrder = order.getTotalAmount().add(unitPrice);
            orderService.updateOrderTotalAmount(order.getId(), totalAmountOfOrder);

            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setOrderID(order);
            newOrderItem.setProductID(product);
            newOrderItem.setQuantity(addOrderItemRequest.getQuantity());
            newOrderItem.setUnitPrice(unitPrice);
            newOrderItem.setCreatedBy(username);
            OrderItem savedOrderItem = orderItemRepository.save(newOrderItem);
//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER_ITEM + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }
            return modelMapper.map(savedOrderItem, OrderItemResponse.class);

        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }


    @Override
    public OrderItemResponse update(UpdateOrderItemRequest updateOrderItemRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();

            OrderItem existingOrderItem = orderItemRepository.findById(updateOrderItemRequest.getOrderItemID())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.OrderItem.ORDER_ITEM_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            if (updateOrderItemRequest.getOrderID() != null) {
                Order order = orderRepository.findById(updateOrderItemRequest.getOrderID())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Order.ORDER_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));
                existingOrderItem.setOrderID(order);
            }

            if (updateOrderItemRequest.getProductID() != null) {
                Product product = productRepository.findById(updateOrderItemRequest.getProductID())
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Product.PRODUCT_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));
                existingOrderItem.setProductID(product);
            }

            if (updateOrderItemRequest.getQuantity() != null) {
                existingOrderItem.setQuantity(updateOrderItemRequest.getQuantity());
            }

            if (updateOrderItemRequest.getUnitPrice() != null) {
                existingOrderItem.setUnitPrice(updateOrderItemRequest.getUnitPrice());
            }

            existingOrderItem.setModifiedBy(username);

            OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER_ITEM + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }

            return modelMapper.map(updatedOrderItem, OrderItemResponse.class);

        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }


    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            OrderItem existingOrderItem = orderItemRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.OrderItem.ORDER_ITEM_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            existingOrderItem.setStatus(ConstStatus.INACTIVE_STATUS);
            orderItemRepository.save(existingOrderItem);

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER_ITEM + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }

            return true;

        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

}
