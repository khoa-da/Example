package com.example.kalban_greenbag.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequest {
    @NotNull(message = "CategoryId is required")
    private UUID id;

    @Size(max = 100, message = "CategoryName cannot exceed 100 characters")
    private String categoryName;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    private String status;
}
