package com.example.kalban_greenbag.service.impl;

import com.example.kalban_greenbag.dto.request.review.CreateReviewRequest;
import com.example.kalban_greenbag.dto.response.review.ReviewResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.IReviewService;

import java.util.UUID;

public class ReviewServiceImpl implements IReviewService {
    @Override
    public ReviewResponse findById(UUID id) throws BaseException {
        return null;
    }

    @Override
    public PagingModel<ReviewResponse> getAll(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public PagingModel<ReviewResponse> findAllByStatusTrue(Integer page, Integer limit) throws BaseException {
        return null;
    }

    @Override
    public ReviewResponse create(CreateReviewRequest createReviewRequest) throws BaseException {
        return null;
    }

    @Override
    public Boolean changeStatus(UUID id) throws BaseException {
        return null;
    }
}
