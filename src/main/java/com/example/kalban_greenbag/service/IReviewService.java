package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.product.CreateProductRequest;
import com.example.kalban_greenbag.dto.request.review.CreateReviewRequest;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.dto.response.review.ReviewResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;

import java.util.List;
import java.util.UUID;

public interface IReviewService extends IGenericService<ReviewResponse>{
    ReviewResponse create(CreateReviewRequest createReviewRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
    PagingModel<ReviewResponse> getAllByProductId(UUID productId, Integer page, Integer limit) throws BaseException;
}
