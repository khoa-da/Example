package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.constant.ConstError;
import com.example.kalban_greenbag.constant.ConstStatus;
import com.example.kalban_greenbag.dto.request.review.CreateReviewRequest;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.dto.response.review.ReviewResponse;
import com.example.kalban_greenbag.entity.Order;
import com.example.kalban_greenbag.entity.Product;
import com.example.kalban_greenbag.entity.Review;
import com.example.kalban_greenbag.enums.ErrorCode;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.repository.OrderRepository;
import com.example.kalban_greenbag.repository.ProductRepository;
import com.example.kalban_greenbag.repository.ReviewRepository;
import com.example.kalban_greenbag.repository.UserRepository;
import com.example.kalban_greenbag.service.IReviewService;
import com.example.kalban_greenbag.service.IUserService;
import com.example.kalban_greenbag.utils.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.kalban_greenbag.entity.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    IUserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;
    @Override
    public ReviewResponse findById(UUID id) throws BaseException {
        try{
            Optional<Review> review = reviewRepository.findById(id);
            if (review.isEmpty()) {
                throw new BaseException(ErrorCode.ERROR_404.getCode(), ConstError.Review.REVIEW_NOT_FOUND, ErrorCode.ERROR_404.getMessage());
            }
            return modelMapper.map(review.get(), ReviewResponse.class);
        }catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<ReviewResponse> getAll(Integer page, Integer limit) throws BaseException {
        try {
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ReviewResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            List<Review> reviewList = reviewRepository.findAllByOrderByCreatedDate(pageable);
            List<ReviewResponse> reviewResponses = reviewList.stream()
                    .map(review -> modelMapper.map(review, ReviewResponse.class))
                    .toList();
            result.setListResult(reviewResponses);
            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
    public int totalItem() {
        return (int) reviewRepository.count();
    }

    @Override
    public PagingModel<ReviewResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        try {
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ReviewResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            List<Review> reviewList = reviewRepository.findAllByStatusOrderByCreatedDate(ConstStatus.ACTIVE_STATUS, pageable);
            List<ReviewResponse> reviewResponses = reviewList.stream()
                    .map(review -> modelMapper.map(review, ReviewResponse.class))
                    .toList();

            result.setListResult(reviewResponses);
            result.setTotalPage((int) Math.ceil((double) totalActiveItems() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }
    private int totalActiveItems() {
        return (int) reviewRepository.countByStatus(ConstStatus.ACTIVE_STATUS);
    }

    @Override
    public ReviewResponse create(CreateReviewRequest createReviewRequest) throws BaseException {
        try {
            String username = SecurityUtil.getCurrentUsername();

            User user = userRepository.findById(createReviewRequest.getUserID())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ConstError.User.USER_NOT_FOUND, ErrorCode.ERROR_404.getMessage()));

            if(!username.equals(user.getUsername())){
                throw new BaseException(ErrorCode.ERROR_403.getCode(),
                        ConstError.User.USER_NOT_AUTHENTICATED, ErrorCode.ERROR_403.getMessage());
            }

            boolean hasValidOrder = orderRepository.existsByUserIdAndStatus(user.getId(), ConstStatus.COMPLETED_STATUS);
            if (!hasValidOrder) {
                throw new BaseException(ErrorCode.ERROR_403.getCode(),
                        ConstError.Order.NO_COMPLETED_ORDERS, ErrorCode.ERROR_403.getMessage());
            }
            Product product = productRepository.findById(createReviewRequest.getProductID())
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ConstError.Product.PRODUCT_NOT_FOUND, ErrorCode.ERROR_404.getMessage()));

            // Ánh xạ CreateReviewRequest sang Review
            Review newReview = new Review();
            newReview.setId(UUID.randomUUID());
            newReview.setRating(createReviewRequest.getRating());
            newReview.setComment(createReviewRequest.getComment());
            newReview.setProductID(product);
            newReview.setUserID(user);
            newReview.setCreatedBy(username);

            // Thiết lập trạng thái cho Review mới
            newReview.setStatus(ConstStatus.ACTIVE_STATUS);

            // Lưu Review vào cơ sở dữ liệu
            Review savedReview = reviewRepository.save(newReview);

            // Trả về ReviewResponse
            return modelMapper.map(savedReview, ReviewResponse.class);
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        try {
            // Tìm review theo id
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new BaseException(ErrorCode.ERROR_404.getCode(),
                            ConstError.Review.REVIEW_NOT_FOUND,
                            ErrorCode.ERROR_404.getMessage()));

            // Thay đổi trạng thái của review
            if (review.getStatus().equals(ConstStatus.ACTIVE_STATUS)) {
                review.setStatus(ConstStatus.INACTIVE_STATUS);
            } else {
                review.setStatus(ConstStatus.ACTIVE_STATUS);
            }

            // Cập nhật trạng thái và lưu lại review
            reviewRepository.save(review);

            return true;
        } catch (Exception exception) {
            if (exception instanceof BaseException) {
                throw exception;
            }
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }

    @Override
    public PagingModel<ReviewResponse> getAllByProductId(UUID productId, Integer page, Integer limit) throws BaseException {
        try {
            if(page == null || limit == null){
                page = 1;
                limit = 10;
            }
            PagingModel<ReviewResponse> result = new PagingModel<>();
            result.setPage(page);
            Pageable pageable = PageRequest.of(page - 1, limit);

            List<Review> reviewList = reviewRepository.findAllByProductIdOrderByCreatedDate(productId, pageable);
            List<ReviewResponse> reviewResponses = reviewList.stream()
                    .map(review -> modelMapper.map(review, ReviewResponse.class))
                    .toList();
            result.setListResult(reviewResponses);
            result.setTotalPage((int) Math.ceil((double) totalItem() / limit));
            result.setLimit(limit);

            return result;
        } catch (Exception exception) {
            throw new BaseException(ErrorCode.ERROR_500.getCode(), exception.getMessage(), ErrorCode.ERROR_500.getMessage());
        }
    }


}
