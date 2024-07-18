package com.example.ddback.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SwipeLikeRequestDto {
    private String userId;
    private String partnerId;
}
