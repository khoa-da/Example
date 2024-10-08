package com.example.kalban_greenbag.dto.response.order_item;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseData extends BaseResponse implements Serializable {
    private String name;
    private Integer quantity;
    private Integer price;

}