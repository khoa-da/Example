package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
//import com.example.kalban_greenbag.constant.ConstHashKeyPrefix;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.order.AddOrderRequest;
import com.example.kalban_greenbag.dto.request.order.UpdateOrderRequest;
import com.example.kalban_greenbag.dto.response.order.OrderResponse;
import com.example.kalban_greenbag.dto.response.order.OrderStatusTotalResponse;
import com.example.kalban_greenbag.dto.response.order.PieChartResponse;
import com.example.kalban_greenbag.dto.response.order_item.OrderItemResponse;
import com.example.kalban_greenbag.entity.Order;
import com.example.kalban_greenbag.entity.OrderItem;
import com.example.kalban_greenbag.entity.User;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.OrderRepository;
import com.example.kalban_greenbag.repository.ProductRepository;
import com.example.kalban_greenbag.repository.ReviewRepository;
import com.example.kalban_greenbag.repository.UserRepository;
import com.example.kalban_greenbag.service.IOrderService;
import com.example.kalban_greenbag.service.IProductService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import com.example.kalban_greenbag.utils.ValidateUtil;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private IProductService productService;


    @Autowired
    private ModelMapper modelMapper;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    public int totalItem() {
        return (int) orderRepository.count();
    }

    @Override
    public OrderResponse findById(UUID id) throws BaseException {
        try {
//            String cacheKey = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + id.toString();
            OrderResponse orderResponse;

//            if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
//                orderResponse = (OrderResponse) redisTemplate.opsForValue().get(cacheKey);
//            } else {
                Order order = orderRepository.findById(id)
                        .orElseThrow(() -> new BaseException(
                                ErrorCode.ERROR_500.getCode(),
                                ConstError.Order.ORDER_NOT_FOUND,
                                ErrorCode.ERROR_500.getMessage()
                        ));

                orderResponse = modelMapper.map(order, OrderResponse.class);
//
//                redisTemplate.opsForValue().set(cacheKey, orderResponse);
//            }

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
//            String cacheKey = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "all:" + page + ":" + limit;

            PagingModel<OrderResponse> result = new PagingModel<>();
            List<OrderResponse> orderResponseList;

//            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, cacheKey)) {
//                orderResponseList = (List<OrderResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, cacheKey);
//            } else {
                Page<Order> ordersPage = orderRepository.findAllByOrderByCreatedDateDesc(pageable);
                orderResponseList = ordersPage.stream()
                        .map(order -> modelMapper.map(order, OrderResponse.class))
                        .toList();

//                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, cacheKey, orderResponseList);
//            }

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

//            String hashKeyForActiveOrders = ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "all:active:" + page + ":" + limit;

            List<OrderResponse> orderResponseList;

//            if (redisTemplate.opsForHash().hasKey(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, hashKeyForActiveOrders)) {
//                orderResponseList = (List<OrderResponse>) redisTemplate.opsForHash().get(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, hashKeyForActiveOrders);
//            } else {
                List<Order> orders = orderRepository.findAllByStatusOrderByCreatedDateDesc(ConstStatus.ACTIVE_STATUS, pageable);
                orderResponseList = orders.stream()
                        .map(order -> {
                            OrderResponse response = modelMapper.map(order, OrderResponse.class);
                            response.setUserId(order.getUserID().getId());
                            return response;
                        })
                        .toList();

//                redisTemplate.opsForHash().put(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER, hashKeyForActiveOrders, orderResponseList);
//            }

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

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }
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

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "*");
//            if (ValidateUtil.IsNotNullOrEmptyForSet(keysToDelete)) {
//                redisTemplate.delete(keysToDelete);
//            }

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

//            Set<String> keysToDelete = redisTemplate.keys(ConstHashKeyPrefix.HASH_KEY_PREFIX_FOR_ORDER + "*");
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
    public PagingModel<OrderResponse> getOrderByUserId(UUID userId, Integer page, Integer limit, String status) throws BaseException {
        try {
            if (page == null || page < 1) {
                page = 1;
            }
            if (limit == null || limit < 1) {
                limit = 10;
            }

            Pageable pageable = PageRequest.of(page - 1, limit);
            Page<Order> orderPage;

            if (ConstStatus.ACTIVE_STATUS.equals(status)) {
                orderPage = orderRepository.findAllByUserIdAndStatusActive(userId, pageable);
            } else if (ConstStatus.INACTIVE_STATUS.equals(status)) {
                orderPage = orderRepository.findAllByUserIdAndStatusInactive(userId, pageable);
            } else {
                orderPage = orderRepository.findAllByUserId(userId, pageable);
            }

            if (orderPage.isEmpty()) {
                PagingModel<OrderResponse> result = new PagingModel<>();
                result.setListResult(Collections.emptyList()); // Danh sách trống
                result.setPage(page);
                result.setTotalPage(0); // Vì không có dữ liệu, tổng số trang là 0
                result.setLimit(limit);
                return result;
            }

            List<OrderResponse> orderResponses = orderPage.stream()
                .map(order -> {
                    OrderResponse response = modelMapper.map(order, OrderResponse.class);
                    response.setUserId(order.getUserID().getId());

                    Set<OrderItem> orderItems = order.getOrderItems();

                    List<OrderItemResponse> orderItemResponses = orderItems.stream()
                        .map(orderItem -> {
                            UUID productId = orderItem.getProductID().getId();

                            boolean isReviewed = reviewRepository.existsByUserIdAndProductId(userId, productId);

                            OrderItemResponse orderItemResponse = modelMapper.map(orderItem, OrderItemResponse.class);
                            orderItemResponse.setIsReview(isReviewed);

                            return orderItemResponse;
                        })
                        .collect(Collectors.toList());

                    Set<OrderItemResponse> orderItemResponsesSet = new HashSet<>(orderItemResponses);
                    response.setOrderItems(orderItemResponsesSet);

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
    public List<PieChartResponse> getPieChartDataForOrderStatus(LocalDate fromDate, LocalDate toDate) throws BaseException {
        try {
            // Convert LocalDate to Instant
            Instant fromInstant = fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant toInstant = toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

            // Fetch orders within the date range
            List<Order> orders = orderRepository.findByOrderDateBetween(fromInstant, toInstant);

            // Group orders by status and count
            Map<String, Long> statusCountMap = orders.stream()
                    .collect(Collectors.groupingBy(Order::getOrderStatus, Collectors.counting()));

            // Convert the map to List<PieChartResponse>
            return statusCountMap.entrySet().stream()
                    .map(entry -> new PieChartResponse(entry.getKey(), entry.getValue().intValue()))
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

    @Override
    public List<PieChartResponse> getPieChartDataForStatus(LocalDate fromDate, LocalDate toDate) throws BaseException {
        try {
            // Convert LocalDate to Instant
            Instant fromInstant = fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            Instant toInstant = toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

            // Fetch orders within the date range
            List<Order> orders = orderRepository.findByOrderDateBetween(fromInstant, toInstant);

            // Group orders by status and count
            Map<String, Long> statusCountMap = orders.stream()
                    .collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));

            // Convert the map to List<PieChartResponse>
            return statusCountMap.entrySet().stream()
                    .map(entry -> new PieChartResponse(entry.getKey(), entry.getValue().intValue()))
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new BaseException(
                    ErrorCode.ERROR_500.getCode(),
                    exception.getMessage(),
                    ErrorCode.ERROR_500.getMessage()
            );
        }
    }

    @Override
    public List<OrderStatusTotalResponse> getTotalAmountAndCountByStatusAndDateRange(String startDate, String endDate) throws BaseException {
        try {
            DateFormat dateFormatInput = new java.text.SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormatOutput = new java.text.SimpleDateFormat("MM-dd-yyyy");

            // Chuyển đổi chuỗi ngày vào kiểu Date
            Date start = dateFormatInput.parse(startDate);
            Date end = dateFormatInput.parse(endDate);

            // Đặt thời gian bắt đầu là đầu ngày
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(start);
            startCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startCalendar.set(Calendar.MINUTE, 0);
            startCalendar.set(Calendar.SECOND, 0);
            startCalendar.set(Calendar.MILLISECOND, 0);
            start = startCalendar.getTime();

            // Đặt thời gian kết thúc là cuối ngày
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(end);
            endCalendar.set(Calendar.HOUR_OF_DAY, 23);
            endCalendar.set(Calendar.MINUTE, 59);
            endCalendar.set(Calendar.SECOND, 59);
            endCalendar.set(Calendar.MILLISECOND, 999);
            end = endCalendar.getTime();

            // Lấy danh sách các đơn hàng trong khoảng thời gian
            List<Order> orders = orderRepository.findByDateRange(start, end);

            // Tạo danh sách để lưu tổng số tiền theo ngày và đếm số lượng đơn hàng
            Map<String, BigDecimal> pendingTotals = new HashMap<>();
            Map<String, BigDecimal> completedTotals = new HashMap<>();
            Map<String, Integer> pendingCounts = new HashMap<>();
            Map<String, Integer> completedCounts = new HashMap<>();

            // Tính tổng số tiền và đếm số lượng cho mỗi ngày
            for (Order order : orders) {
                String dateKey = dateFormatOutput.format(order.getCreatedDate()); // Định dạng ngày

                // Tính tổng theo trạng thái và đếm
                if (order.getOrderStatus().equals("PENDING")) {
                    pendingTotals.put(dateKey, pendingTotals.getOrDefault(dateKey, BigDecimal.ZERO).add(order.getTotalAmount()));
                    pendingCounts.put(dateKey, pendingCounts.getOrDefault(dateKey, 0) + 1);
                } else if (order.getOrderStatus().equals("COMPLETED")) {
                    completedTotals.put(dateKey, completedTotals.getOrDefault(dateKey, BigDecimal.ZERO).add(order.getTotalAmount()));
                    completedCounts.put(dateKey, completedCounts.getOrDefault(dateKey, 0) + 1);
                }
            }

            // Chuẩn bị danh sách phản hồi
            List<OrderStatusTotalResponse> responseList = new ArrayList<>();
            for (String date : pendingTotals.keySet()) {
                BigDecimal pendingTotal = pendingTotals.getOrDefault(date, BigDecimal.ZERO);
                BigDecimal completedTotal = completedTotals.getOrDefault(date, BigDecimal.ZERO);
                int pendingCount = pendingCounts.getOrDefault(date, 0);
                int completedCount = completedCounts.getOrDefault(date, 0);
                responseList.add(new OrderStatusTotalResponse(date, pendingTotal, pendingCount, completedTotal, completedCount));
            }

            // Sắp xếp danh sách phản hồi nếu cần (tùy chọn)
            responseList.sort(Comparator.comparing(OrderStatusTotalResponse::getDate));

            // Đảo ngược thứ tự của danh sách phản hồi
            Collections.reverse(responseList);

            return responseList;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), "Failed to retrieve total amounts and counts.", e.getMessage());
        }
    }

}

