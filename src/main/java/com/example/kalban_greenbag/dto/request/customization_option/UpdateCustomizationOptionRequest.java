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
public class UpdateCustomizationOptionRequest {
    @NotNull
    private UUID id;

    @Size(max = 50)
    private String optionName;

    @Size(max = 20)
    private String optionType;

    @DecimalMin(value = "0.00", inclusive = false, message = "Additional price must be positive")
    private BigDecimal additionalPrice;

    private UUID baseModelID;
}
