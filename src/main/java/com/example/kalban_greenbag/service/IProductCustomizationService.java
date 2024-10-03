package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.product_customization.AddProductCustomizationRequest;
import com.example.kalban_greenbag.dto.request.product_customization.UpdateProductCustomizationRequest;
import com.example.kalban_greenbag.dto.response.product_customization.ProductCustomizationResponse;
import com.example.kalban_greenbag.entity.ProductCustomization;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;

import java.util.UUID;

public interface IProductCustomizationService extends IGenericService<ProductCustomizationResponse>{
    ProductCustomizationResponse create(AddProductCustomizationRequest productCustomization) throws BaseException;
    ProductCustomizationResponse update(UpdateProductCustomizationRequest productCustomization) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
    PagingModel<ProductCustomizationResponse> getProductCustomByUserId(UUID userId , Integer page, Integer limit) throws BaseException;
}