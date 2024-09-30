package com.example.kalban_greenbag.dto.request.product_customization;

import com.example.kalban_greenbag.entity.CustomizationOption;
import com.example.kalban_greenbag.entity.Product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddProductCustomizationRequest {
    private UUID productId;
    private UUID optionId;
    private String imageURL;
    private String customValue;
    private String status;
}
