package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.base_model.AddBaseModelRequest;
import com.example.kalban_greenbag.dto.request.base_model.UpdateBaseModelRequest;
import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.request.category.UpdateCategoryRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.exception.BaseException;

import java.util.UUID;

public interface IBaseModelService extends IGenericService<BaseModelResponse>{
    BaseModelResponse create(AddBaseModelRequest addBaseModelRequest) throws BaseException;
    BaseModelResponse update(UpdateBaseModelRequest updateBaseModelRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
}
