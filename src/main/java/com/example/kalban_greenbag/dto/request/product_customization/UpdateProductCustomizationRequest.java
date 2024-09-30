package com.example.kalban_greenbag.dto.request.product_customization;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductCustomizationRequest {
    private UUID id;
    private UUID productId;
    private UUID optionId;
    private String imageURL;
    private String status;
    private String customValue;
}
