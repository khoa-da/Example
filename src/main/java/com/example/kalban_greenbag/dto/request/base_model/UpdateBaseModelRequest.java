package com.example.kalban_greenbag.dto.request.base_model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBaseModelRequest {
    @NotNull
    private UUID id;

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Nationalized
    private String modelName;

    private String imageURL;

    @Nationalized
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @DecimalMin(value = "0.00", message = "Base price must be at least 0")
    private BigDecimal basePrice;

    private UUID categoryID;
}
