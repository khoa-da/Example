package com.example.kalban_greenbag.service;

import com.example.kalban_greenbag.dto.request.customization_option.AddCustomizationOptionRequest;
import com.example.kalban_greenbag.dto.request.customization_option.UpdateCustomizationOptionRequest;
import com.example.kalban_greenbag.dto.response.customization_option.CustomizationOptionResponse;
import com.example.kalban_greenbag.exception.BaseException;

import java.util.UUID;

public interface ICustomizationOptionService extends IGenericService<CustomizationOptionResponse>{
    CustomizationOptionResponse create(AddCustomizationOptionRequest addCustomizationOptionRequest) throws BaseException;
    CustomizationOptionResponse update(UpdateCustomizationOptionRequest updateCustomizationOptionRequest) throws BaseException;
    Boolean changeStatus(UUID id) throws BaseException;
}
