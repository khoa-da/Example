package com.example.kalban_greenbag.dto.response.review;

import com.example.kalban_greenbag.dto.response.BaseResponse;
import com.example.kalban_greenbag.entity.Product;
import com.example.kalban_greenbag.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse extends BaseResponse {

    private UUID productID;

    private UUID userID;


    private Integer rating;


    private String comment;
}
