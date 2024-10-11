package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.request.category.UpdateCategoryRequest;
import com.example.kalban_greenbag.dto.request.product.CreateProductRequest;
import com.example.kalban_greenbag.dto.request.product.UpdateProductRequest;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;

import java.math.BigDecimal;
import java.util.UUID;

public interface IProductService extends IGenericService<ProductResponse>{
    ProductResponse create(CreateProductRequest createProductRequest) throws BaseException;
    ProductResponse update(UpdateProductRequest updateProductRequest) throws BaseException;
    Boolean changeStatus(UUID productId) throws BaseException;
    boolean reduceProductStock(UUID productId, Integer stock) throws BaseException;
    PagingModel<ProductResponse> getProductByName(String name, Integer page, Integer limit) throws BaseException;
    PagingModel<ProductResponse> getProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer limit) throws BaseException;
    PagingModel<ProductResponse> getProductByNameAndPriceRange(String name, BigDecimal minPrice, BigDecimal maxPrice, Integer page, Integer limit) throws BaseException;

}
