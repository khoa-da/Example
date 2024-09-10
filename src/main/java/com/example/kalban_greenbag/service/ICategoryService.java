package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.request.category.CategoryRequest;
import com.example.kalban_greenbag.dto.request.category.UpdateCategoryRequest;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.exception.BaseException;

import java.util.UUID;

public interface ICategoryService extends IGenericService<CategoryResponse>{
    CategoryResponse create(AddCategoryRequest addCategoryRequest) throws BaseException;
    CategoryResponse update(UpdateCategoryRequest updateCategoryRequest) throws BaseException;
    Boolean changeStatus(UUID categoryId) throws BaseException;
}
