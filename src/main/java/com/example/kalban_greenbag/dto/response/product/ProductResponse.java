package com.example.kalban_greenbag.dto.response.product;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.entity.BaseModel;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {

    private UUID baseModelID;

    private BigDecimal finalPrice;
}
