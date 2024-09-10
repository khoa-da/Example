package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.product.CreateProductRequest;
import com.example.kalban_greenbag.dto.request.review.CreateReviewRequest;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.dto.response.review.ReviewResponse;
import com.example.kalban_greenbag.exception.BaseException;

import java.util.UUID;

public interface IReviewService extends IGenericService<ReviewResponse>{
    ReviewResponse create(CreateReviewRequest createReviewRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
}
