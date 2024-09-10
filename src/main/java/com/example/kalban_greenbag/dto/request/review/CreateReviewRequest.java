package com.example.kalban_greenbag.dto.request.review;

import com.example.kalban_greenbag.entity.User;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewRequest{
    private UUID productID;
    private Integer rating;
    private String comment;
    private UUID userID;

}
