package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.product.CreateProductRequest;
import com.example.kalban_greenbag.dto.request.product.UpdateProductRequest;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.IProductService;
import com.example.kalban_greenbag.service.IJWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Product Controller")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IJWTService jwtService;

    @Operation(summary = "Create product", description = "API create product")
    @PostMapping(value = ConstAPI.ProductAPI.CREATE_PRODUCT)
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest createProductRequest,
                                         HttpServletRequest request) throws BaseException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = jwtService.extractBearerToken(authorizationHeader);
        return productService.create(createProductRequest);
    }

    @Operation(summary = "Update product", description = "API update product")
    @PatchMapping(value = ConstAPI.ProductAPI.UPDATE_PRODUCT)
    public ProductResponse updateProduct(@Valid @RequestBody UpdateProductRequest updateProductRequest) throws BaseException {
        return productService.update(updateProductRequest);
    }

    @Operation(summary = "Delete product", description = "API delete product")
    @DeleteMapping(value = ConstAPI.ProductAPI.DELETE_PRODUCT + "{id}")
    public Boolean deleteProduct(@PathVariable("id") UUID id) throws BaseException {
        return productService.changeStatus(id);
    }

    @Operation(summary = "Get product by id", description = "API find product by id")
    @GetMapping(value = ConstAPI.ProductAPI.GET_PRODUCT_BY_ID + "{id}")
    public ProductResponse findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting product with id: {}", id);
            return productService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }
    }

    @Operation(summary = "Get all products", description = "API get all products")
    @GetMapping(value = ConstAPI.ProductAPI.GET_ALL_PRODUCT)
    public PagingModel getAllProducts(@RequestParam(value = "page", required = false) Integer page,
                                      @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all products with page: {}, limit: {}", page, limit);
        return productService.getAll(page, limit);
    }

    @Operation(summary = "Get all products by status active", description = "API get all products by status active")
    @GetMapping(value = ConstAPI.ProductAPI.GET_ALL_PRODUCT_STATUS_TRUE)
    public PagingModel getAllProductsByStatusActive(@RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active products with page: {}, limit: {}", page, limit);
        return productService.findAllByStatusTrue(page, limit);
    }
}