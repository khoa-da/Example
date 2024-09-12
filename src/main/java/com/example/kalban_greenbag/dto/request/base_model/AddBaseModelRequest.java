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
public class AddBaseModelRequest {
    @Size(max = 100)
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
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull
    private UUID categoryID;
}
