package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.base_model.AddBaseModelRequest;
import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.response.base_model.BaseModelResponse;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.service.IBaseModelService;
import com.example.kalban_greenbag.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "BaseModel Controller")
public class BaseModelController {
    @Autowired
    private IBaseModelService baseModelService;

    @Operation(summary = "Create Base Model", description = "API create Base Model")
    @PostMapping(value = ConstAPI.BaseModelAPI.CREATE_BASE_MODEL)
    public BaseModelResponse createBaseModelResponse(@Valid @RequestBody AddBaseModelRequest addBaseModelRequest) throws BaseException {
        return baseModelService.create(addBaseModelRequest);
    }
}
