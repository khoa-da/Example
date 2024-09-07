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
    @NotNull
    @Nationalized
    private String modelName;

    @Nationalized
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @DecimalMin(value = "0.00", message = "Base price must be at least 0")
//    @DecimalMax(value = "1000000.00", message = "Base price must be less than or equal to 10,000")
    @NotNull(message = "Base price cannot be null")
    private BigDecimal basePrice;

    @NotNull
    private UUID categoryID;
}
