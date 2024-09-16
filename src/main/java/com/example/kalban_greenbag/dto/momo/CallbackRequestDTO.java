package com.example.kalban_greenbag.dto.momo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackRequestDTO {

    private Long amount;
    private String orderId;
    private String orderInfo;

}

