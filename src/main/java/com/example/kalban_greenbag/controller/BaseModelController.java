package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.base_model.AddBaseModelRequest;
import com.example.kalban_greenbag.dto.request.base_model.UpdateBaseModelRequest;
import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.request.category.UpdateCategoryRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.IBaseModelService;
import com.example.kalban_greenbag.service.ICategoryService;
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
@Tag(name = "BaseModel Controller")
public class BaseModelController {
    @Autowired
    private IBaseModelService baseModelService;

    @Operation(summary = "Create base model", description = "API create base model")
    @PostMapping(value = ConstAPI.BaseModelAPI.CREATE_BASE_MODEL)
    public BaseModelResponse createBaseModelResponse(@Valid @RequestBody AddBaseModelRequest addBaseModelRequest) throws BaseException {
        return baseModelService.create(addBaseModelRequest);
    }

    @Operation(summary = "Update base model", description = "API update base model")
    @PatchMapping(value = ConstAPI.BaseModelAPI.UPDATE_BASE_MODEL)
    public BaseModelResponse updateBaseModel(@Valid @RequestBody UpdateBaseModelRequest updateBaseModelRequest) throws BaseException {
        return baseModelService.update(updateBaseModelRequest);
    }

    @Operation(summary = "Delete base model", description = "API delete base model")
    @DeleteMapping(value = ConstAPI.BaseModelAPI.DELETE_BASE_MODEL + "{id}")
    public Boolean deleteBaseModel(@PathVariable("id") UUID id) throws BaseException {
        return baseModelService.changeStatus(id);
    }

    @Operation(summary = "Get base model by id", description = "API find base model by id")
    @GetMapping(value = ConstAPI.BaseModelAPI.GET_BASE_MODEL_BY_ID + "{id}")
    public BaseModelResponse findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            return baseModelService.findById(id);
        } catch (Exception e) {
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Get all base model", description = "API get all base model")
    @GetMapping(value = ConstAPI.BaseModelAPI.GET_ALL_BASE_MODELS)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return baseModelService.getAll(page, limit);
    }

    @Operation(summary = "Get all active base model", description = "API get all active base model")
    @GetMapping(value = ConstAPI.BaseModelAPI.GET_ALL_BASE_MODELS_STATUS_ACTIVE)
    public PagingModel getAllStatusTrue(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        return baseModelService.findAllByStatusTrue(page, limit);
    }
}
