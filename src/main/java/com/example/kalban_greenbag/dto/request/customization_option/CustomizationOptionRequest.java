package com.example.kalban_greenbag.dto.request.customization_option;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomizationOptionRequest {
    @NotNull(message = "Option name cannot be null")
    @Size(max = 50, message = "Option name cannot exceed 50 characters")
    private String optionName;

    @NotNull(message = "Option type cannot be null")
    @Size(max = 20, message = "Option type cannot exceed 20 characters")
    private String optionType;

    @DecimalMin(value = "0.00", inclusive = false, message = "Additional price must be positive")
    private BigDecimal additionalPrice;

    @NotNull(message = "Base model ID cannot be null")
    private UUID baseModelID;
}
