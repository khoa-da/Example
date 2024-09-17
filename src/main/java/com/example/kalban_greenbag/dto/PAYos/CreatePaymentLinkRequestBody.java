package com.example.kalban_greenbag.dto.PAYos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.payos.type.ItemData;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentLinkRequestBody {
    private UUID orderId;
    private String description;
    private String returnUrl;
    private String cancelUrl;
}
