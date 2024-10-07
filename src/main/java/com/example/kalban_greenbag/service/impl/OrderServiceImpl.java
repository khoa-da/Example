package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.order.AddOrderRequest;
import com.example.kalban_greenbag.dto.request.order.UpdateOrderRequest;
import com.example.kalban_greenbag.dto.response.order.OrderResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.entity.Order;
import com.example.kalban_greenbag.entity.OrderItem;
import com.example.kalban_greenbag.entity.User;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.OrderRepository;
import com.example.kalban_greenbag.repository.ProductRepository;
import com.example.kalban_greenbag.repository.UserRepository;
import com.example.kalban_greenbag.service.IOrderService;
import com.example.kalban_greenbag.service.IProductService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import com.example.kalban_greenbag.utils.ValidateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public int totalItem() {
        return (int) orderRepository.count();
    }

    @Override
    public OrderResponse findById(UUID id) throws BaseException {
        try {
            String cacheKey = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + id.toString();
            OrderResponse orderResponse;

            if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
                orderResponse = (OrderResponse) redisTemplate.opsForValue().get(cacheKey);
            } else {
                Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Order.ORDER_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));

                orderResponse = modelMapper.map(order, OrderResponse.class);

                redisTemplate.opsForValue().set(cacheKey, orderResponse);
            }

            return orderResponse;

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
    public PagingModel<OrderResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            Pageable pageable = PageRequest.of(page - 1, limit);
            String cacheKey = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "all:" + page + ":" + limit;

            PagingModel<OrderResponse> result = new PagingModel<>();
            List<OrderResponse> orderResponseList;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, cacheKey)) {
                orderResponseList = (List<OrderResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, cacheKey);
            } else {
                Page<Order> ordersPage = orderRepository.findAllByOrderByCreatedDate(pageable);
                orderResponseList = ordersPage.stream()
                        .map(order -> modelMapper.map(order, OrderResponse.class))
                        .toList();

                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, cacheKey, orderResponseList);
            }

            result.setPage(page);
            result.setListResult(orderResponseList);
            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
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
    public PagingModel<OrderResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            PagingModel<OrderResponse> result = new PagingModel<>();
            result.setPage(page);

            Pageable pageable = PageRequest.of(page - 1, limit);

            String hashKeyForActiveOrders = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "all:active:" + page + ":" + limit;

            List<OrderResponse> orderResponseList;

            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, hashKeyForActiveOrders)) {
                orderResponseList = (List<OrderResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, hashKeyForActiveOrders);
            } else {
                List<Order> orders = orderRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
                orderResponseList = orders.stream()
                        .map(order -> {
                            OrderResponse response = modelMapper.map(order, OrderResponse.class);
                            response.setUserId(order.getUserID().getId());
                            return response;
                        })
                        .toList();

                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, hashKeyForActiveOrders, orderResponseList);
            }

            result.setListResult(orderResponseList);

            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


    @Override
    public OrderResponse create(AddOrderRequest addOrderRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();
            Order newOrder = new Order();
            User user = userRepository.findById(addOrderRequest.getUserID())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.User.USER_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));
            newOrder.setUserID(user);
            newOrder.setStatus(addOrderRequest.getStatus());
            newOrder.setOrderStatus(addOrderRequest.getOrderStatus());
            newOrder.setOrderDate(Instant.now());
            newOrder.setReason(addOrderRequest.getReason());
            newOrder.setTotalAmount(addOrderRequest.getTotalAmount());
            newOrder.setShippingAddress(addOrderRequest.getShippingAddress());
            newOrder.setCreatedBy(username);
            Order savedOrder = orderRepository.save(newOrder);

            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }
            return modelMapper.map(savedOrder, OrderResponse.class);
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
    public OrderResponse update(UpdateOrderRequest updateOrderRequest) throws BaseException {
        try {
            Order order = orderRepository.findById(updateOrderRequest.getOrderID())
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_500.getCode(),
                            ConstError.Order.ORDER_NOT_FOUND,
                            ErrorCode.ERROR_500.getMessage()
                    ));

            if (updateOrderRequest.getOrderDate() != null) {
                order.setOrderDate(updateOrderRequest.getOrderDate());
            }
            if (updateOrderRequest.getTotalAmount() != null) {
                order.setTotalAmount(updateOrderRequest.getTotalAmount());
            }
            if (updateOrderRequest.getShippingAddress() != null) {
                order.setShippingAddress(updateOrderRequest.getShippingAddress());
            }
            if (updateOrderRequest.getOrderStatus() != null) {
                order.setStatus(updateOrderRequest.getOrderStatus());
            }
            if (updateOrderRequest.getStatus() != null) {
                order.setStatus(updateOrderRequest.getStatus());
            }
            if (updateOrderRequest.getReason() != null) {
                order.setStatus(updateOrderRequest.getReason());
            }
            order.setModifiedBy(SecurityUtil.getCurrentUsername());

            Order updatedOrder = orderRepository.save(order);

            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

            return modelMapper.map(updatedOrder, OrderResponse.class);
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
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new BaseException(
                            ErrorCode.ERROR_404.getCode(),
                            ConstError.Order.ORDER_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()
                    ));

            order.setStatus(ConstStatus.INACTIVE_STATUS);

            orderRepository.save(order);

            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "*");
            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
                redisTemplate.delete(keysToDelete);
            }

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

    @Override
    public void updateOrderTotalAmount(UUID orderId, BigDecimal totalAmount) throws BaseException {
        try{
            orderRepository.updateTotalAmount(orderId, totalAmount);
        } catch (Exception exception) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

    @Override
    public PagingModel<OrderResponse> getOrderByOrderCode(long orderCode, Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            Pageable pageable = PageRequest.of(page - 1, limit);

            // Fetching the order by order code
            Page<Order> orderPage = orderRepository.findByOrderCode(orderCode, pageable);

            if (orderPage.isEmpty()) {
                throw new BaseException(ErrorCode.ERROR_500.getCode(), ConstError.Order.ORDER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            List<OrderResponse> orderResponses = orderPage.stream()
                    .map(order -> {
                        OrderResponse response = modelMapper.map(order, OrderResponse.class);
                        response.setUserId(order.getUserID().getId());
                        return response;
                    })
                    .toList();

            PagingModel<OrderResponse> result = new PagingModel<>();
            result.setListResult(orderResponses);
            result.setPage(page);
            result.setTotalPage(orderPage.getTotalPages());
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


    @Override
    public PagingModel<OrderResponse> getOrderByUserId(UUID userId, Integer page, Integer limit) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            Pageable pageable = PageRequest.of(page - 1, limit);

            // Fetching the orders by user ID
            Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);

            if (orderPage.isEmpty()) {
                throw new BaseException(ErrorCode.ERROR_404.getCode(), ConstError.Order.ORDER_NOT_FOUND, ErrorCode.ERROR_500.getMessage());
            }

            List<OrderResponse> orderResponses = orderPage.stream()
                    .map(order -> {
                        OrderResponse response = modelMapper.map(order, OrderResponse.class);
                        response.setUserId(order.getUserID().getId());
                        return response;
                    })
                    .toList();

            PagingModel<OrderResponse> result = new PagingModel<>();
            result.setListResult(orderResponses);
            result.setPage(page);
            result.setTotalPage(orderPage.getTotalPages());
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


}
