package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.category.AddCategoryRequest;
import com.example.kalban_greenbag.dto.request.category.CategoryRequest;
import com.example.kalban_greenbag.dto.request.category.UpdateCategoryRequest;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.ICategoryService;
import com.example.kalban_greenbag.service.IJWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@Tag(name = "Category Controller")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IJWTService jwtService;

    @Operation(summary = "Create category", description = "API create category")
    @PostMapping(value = ConstAPI.CategoryAPI.CREATE_CATEGORY)
    public CategoryResponse createCategory(@Valid @RequestBody AddCategoryRequest addCategoryRequest) throws BaseException {
        return categoryService.create(addCategoryRequest);
    }

    @Operation(summary = "Update category", description = "API update category")
    @PatchMapping(value = ConstAPI.CategoryAPI.UPDATE_CATEGORY)
    public CategoryResponse updateCategory(@Valid @RequestBody UpdateCategoryRequest updateCategoryRequest) throws BaseException {
        return categoryService.update(updateCategoryRequest);
    }

    @Operation(summary = "Delete category", description = "API delete category")
    @DeleteMapping(value = ConstAPI.CategoryAPI.DELETE_CATEGORY + "{id}")
    public Boolean deleteCategory(@PathVariable("id") UUID id) throws BaseException {
        return categoryService.changeStatus(id);
    }

    @Operation(summary = "Get category by id", description = "API find category by id")
    @GetMapping(value = ConstAPI.CategoryAPI.GET_CATEGORY_BY_ID + "{id}")
    public CategoryResponse findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting category with id: {}", id);
            return categoryService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Get all categories", description = "API get all categories")
    @GetMapping(value = ConstAPI.CategoryAPI.GET_ALL_CATEGORY)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all categories with page: {}, limit: {}", page, limit);
        return categoryService.getAll(page, limit);
    }

    @Operation(summary = "Get all categories by status active", description = "API get all categories by status active")
    @GetMapping(value = ConstAPI.CategoryAPI.GET_ALL_CATEGORY_STATUS_TRUE)
    public PagingModel getAllByStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all categories with page: {}, limit: {}", page, limit);
        return categoryService.findAllByStatusTrue(page, limit);
    }

}
