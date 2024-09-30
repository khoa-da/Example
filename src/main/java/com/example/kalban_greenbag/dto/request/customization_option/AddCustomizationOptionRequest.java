package com.example.kalban_greenbag.dto.request.customization_option;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
public class AddCustomizationOptionRequest {

    @NotNull(message = "Option Name is required")
    @NotBlank(message = "Option Name cannot be blank")
    @Size(max = 50, message = "Option Name cannot exceed 50 characters")
    private String optionName;

    @Size(max = 20)
    @NotNull
    @NotBlank(message = "Option Type cannot be blank")
    private String optionType;

    @NotNull
    @NotBlank(message = "Status cannot be blank")
    private String status;

    @NotNull
    private BigDecimal additionalPrice;

    @NotNull
    private UUID baseModelID;

}
