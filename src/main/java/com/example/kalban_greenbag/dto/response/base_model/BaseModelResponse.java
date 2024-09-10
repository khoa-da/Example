package com.example.kalban_greenbag.dto.response.base_model;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.dto.response.category.CategoryResponse;
import com.example.kalban_greenbag.dto.response.customization_option.CustomizationOptionResponse;
import com.example.kalban_greenbag.dto.response.product.ProductResponse;
import com.example.kalban_greenbag.entity.Category;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseModelResponse extends BaseResponse implements Serializable {

    private String modelName;

    private String description;

    private BigDecimal basePrice;

//    private Category category;

    private Set<CustomizationOptionResponse> customizationOptions;

    private Set<ProductResponse> products;
}
