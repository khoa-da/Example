package com.example.kalban_greenbag.dto.response.category;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse extends BaseResponse implements Serializable {
    private String status;
    private String categoryName;
    private String description;
}
