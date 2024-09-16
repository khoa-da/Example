package com.example.kalban_greenbag.controller;

import com.example.kalban_greenbag.dto.momo.OrderRequestDTO;
import com.example.kalban_greenbag.service.momo.CreateOrderPaymentService;
import com.example.kalban_greenbag.service.momo.GetPaymentStatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "MomoPayment Controller")
public class MomoPaymentController {
    @Autowired
    private GetPaymentStatusService paymentStatusService;

    @Autowired
    private CreateOrderPaymentService paymentService;

    @PostMapping("/api/v1/momo-payment")
    public ResponseEntity<Map<String, Object>> momoPayment(@RequestBody OrderRequestDTO orderRequest) throws NoSuchAlgorithmException, InvalidKeyException, IOException {

        Map<String, Object> result = this.paymentService.createOrder(orderRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    @PostMapping("/api/v1/get-status")
    public ResponseEntity<Map<String, Object>> getStatus(@RequestBody OrderRequestDTO requestDTO) throws IOException {

        Map<String, Object> result = this.paymentStatusService.getStatus(requestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    @GetMapping("api/v1/callback")
    public ResponseEntity<Map<String, Object>> callBack(@RequestParam Map<String, Object> callbackRequestDTO) {

        Map<String, Object> result = new HashMap<>();
        if (callbackRequestDTO.containsKey("message")
                && callbackRequestDTO.get("message").equals("Success")) {

            result.put("orderId", callbackRequestDTO.get("orderId"));
            result.put("amount", callbackRequestDTO.get("amount"));
            result.put("orderInfo", callbackRequestDTO.get("orderInfo"));
            result.put("message", callbackRequestDTO.get("message"));
        } else {

            result.put("message", callbackRequestDTO.get("message"));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
