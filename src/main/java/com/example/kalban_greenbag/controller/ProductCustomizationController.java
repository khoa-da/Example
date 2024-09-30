package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.constant.ConstAPI;
import com.example.kalban_greenbag.dto.request.product_customization.AddProductCustomizationRequest;
import com.example.kalban_greenbag.dto.request.product_customization.UpdateProductCustomizationRequest;
import com.example.kalban_greenbag.dto.response.product_customization.ProductCustomizationResponse;
import com.example.kalban_greenbag.exception.BaseException;
import com.example.kalban_greenbag.model.PagingModel;
import com.example.kalban_greenbag.service.IProductCustomizationService;
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
@Tag(name = "Product Customization Controller")
public class ProductCustomizationController {

    @Autowired
    private IProductCustomizationService productCustomizationService;

    @Operation(summary = "Add product customization", description = "API to add a product customization")
    @PostMapping(value = ConstAPI.ProductCustomizationAPI.CREATE_PRODUCT_CUSTOMIZATION)
    public ProductCustomizationResponse addProductCustomization(@Valid @RequestBody AddProductCustomizationRequest request) throws BaseException {
        log.info("Adding product customization: {}", request);
        return productCustomizationService.create(request);
    }

    @Operation(summary = "Update product customization", description = "API to update a product customization")
    @PatchMapping(value = ConstAPI.ProductCustomizationAPI.UPDATE_PRODUCT_CUSTOMIZATION)
    public ProductCustomizationResponse updateProductCustomization(@Valid @RequestBody UpdateProductCustomizationRequest request) throws BaseException {
        log.info("Updating product customization: {}", request);
        return productCustomizationService.update(request);
    }

    @Operation(summary = "Delete product customization", description = "API to delete (deactivate) a product customization")
    @DeleteMapping(value = ConstAPI.ProductCustomizationAPI.DELETE_PRODUCT_CUSTOMIZATION + "{id}")
    public Boolean deleteProductCustomization(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting product customization with id: {}", id);
        return productCustomizationService.changeStatus(id);
    }

    @Operation(summary = "Find product customization by ID", description = "API to get a product customization by ID")
    @GetMapping(value = ConstAPI.ProductCustomizationAPI.GET_PRODUCT_CUSTOMIZATION_BY_ID + "{id}")
    public ProductCustomizationResponse findById(@PathVariable("id") UUID id) throws BaseException {
        log.info("Fetching product customization with id: {}", id);
        return productCustomizationService.findById(id);
    }

    @Operation(summary = "Get all product customizations", description = "API to get all product customizations")
    @GetMapping(value = ConstAPI.ProductCustomizationAPI.GET_ALL_PRODUCT_CUSTOMIZATIONS)
    public PagingModel<ProductCustomizationResponse> getAllCustomizations(@RequestParam(value = "page", required = false) Integer page,
                                                                          @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Fetching all product customizations with page: {}, limit: {}", page, limit);
        return productCustomizationService.getAll(page, limit);
    }

    @Operation(summary = "Get all active product customizations", description = "API to get all active product customizations")
    @GetMapping(value = ConstAPI.ProductCustomizationAPI.GET_ACTIVE_PRODUCT_CUSTOMIZATIONS)
    public PagingModel<ProductCustomizationResponse> getActiveCustomizations(@RequestParam(value = "page", required = false) Integer page,
                                                                             @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Fetching all active product customizations with page: {}, limit: {}", page, limit);
        return productCustomizationService.findAllByStatusTrue(page, limit);
    }
}
