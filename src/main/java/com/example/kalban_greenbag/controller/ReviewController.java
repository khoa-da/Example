package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.product.CreateProductRequest;
import com.example.kalban_greenbag.dto.request.review.CreateReviewRequest;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.dto.response.review.ReviewResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Review Controller")
public class ReviewController {

    @Autowired
    IReviewService reviewService;

    @Operation(summary = "Create review", description = "API create review")
    @PostMapping(value = ConstAPI.ReviewAPI.CREATE_REVIEW)
    public ReviewResponse createReview(@Valid @RequestBody CreateReviewRequest createReviewRequest) throws BaseException {


        return reviewService.create(createReviewRequest);
    }

    @Operation(summary = "Find review by ID", description = "API to get a review by its ID")
    @GetMapping(value = ConstAPI.ReviewAPI.GET_REVIEW_BY_ID + "/{id}")
    public ReviewResponse findById(@PathVariable UUID id) throws BaseException {
        return reviewService.findById(id);
    }

    @Operation(summary = "Get all reviews", description = "API to get a paginated list of all reviews")
    @GetMapping(value = ConstAPI.ReviewAPI.GET_ALL_REVIEWS)
    public PagingModel<ReviewResponse> getAllReviews(@RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return reviewService.getAll(page, limit);
    }

    @Operation(summary = "Get all active reviews", description = "API to get a paginated list of active reviews")
    @GetMapping(value = ConstAPI.ReviewAPI.GET_ALL_REVIEWS_STATUS_ACTIVE)
    public PagingModel<ReviewResponse> getAllActiveReviews(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return reviewService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Change review status", description = "API to change the status of a review (active/inactive)")
    @PatchMapping(value = ConstAPI.ReviewAPI.DELETE_REVIEW + "/{id}")
    public Boolean changeStatus(@PathVariable UUID id) throws BaseException {
        return reviewService.changeStatus(id);
    }

    @Operation(summary = "Get reviews by product ID", description = "API to get a paginated list of reviews by product ID")
    @GetMapping(value = ConstAPI.ReviewAPI.GET_REVIEWS_BY_PRODUCT_ID + "/{productId}")
    public PagingModel<ReviewResponse> getAllByProductId(@PathVariable UUID productId,
                                                  @RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return reviewService.getAllByProductId(productId, page, limit);
    }
}
