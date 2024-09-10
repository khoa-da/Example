package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.customization_option.AddCustomizationOptionRequest;
import com.example.kalban_greenbag.dto.request.customization_option.UpdateCustomizationOptionRequest;
import com.example.kalban_greenbag.dto.response.customization_option.CustomizationOptionResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.ICustomizationOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Customization Option Controller")
public class CustomizationOptionController {

    @Autowired
    private ICustomizationOptionService customizationOptionService;

    @Operation(summary = "Create customization option", description = "API create customization option")
    @PostMapping(value = ConstAPI.CustomizationOptionAPI.CREATE_CUSTOMIZATION_OPTION)
    public CustomizationOptionResponse createCustomizationOption(@Valid @RequestBody AddCustomizationOptionRequest addCustomizationOptionRequest) throws BaseException {
        return customizationOptionService.create(addCustomizationOptionRequest);
    }

    @Operation(summary = "Update customization option", description = "API update customization option")
    @PatchMapping(value = ConstAPI.CustomizationOptionAPI.UPDATE_CUSTOMIZATION_OPTION)
    public CustomizationOptionResponse updateCustomizationOption(@Valid @RequestBody UpdateCustomizationOptionRequest updateCustomizationOptionRequest) throws BaseException {
        return customizationOptionService.update(updateCustomizationOptionRequest);
    }

    @Operation(summary = "Delete customization option", description = "API delete customization option")
    @DeleteMapping(value = ConstAPI.CustomizationOptionAPI.DELETE_CUSTOMIZATION_OPTION + "{id}")
    public Boolean deleteCustomizationOption(@PathVariable("id") UUID id) throws BaseException {
        return customizationOptionService.changeStatus(id);
    }

    @Operation(summary = "Get customization option by id", description = "API find customization option by id")
    @GetMapping(value = ConstAPI.CustomizationOptionAPI.GET_CUSTOMIZATION_OPTION_BY_ID + "{id}")
    public CustomizationOptionResponse findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            return customizationOptionService.findById(id);
        } catch (Exception e) {
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Get all customization options", description = "API get all customization options")
    @GetMapping(value = ConstAPI.CustomizationOptionAPI.GET_ALL_CUSTOMIZATION_OPTION)
    public PagingModel<CustomizationOptionResponse> getAll(@RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return customizationOptionService.getAll(page, limit);
    }

    @Operation(summary = "Get all customization options by status active", description = "API get all customization options by status active")
    @GetMapping(value = ConstAPI.CustomizationOptionAPI.GET_ALL_CUSTOMIZATION_OPTION_STATUS_TRUE)
    public PagingModel<CustomizationOptionResponse> getAllByStatusActive(@RequestParam(value = "page", required = false) Integer page,
                                                                         @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return customizationOptionService.findAllByStatusTrue(page, limit);
    }
}
