package com.example.ddback.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchResponseDto {
    private String userId;
    private String nickname;
    private String imageLink;
    private String matchedTime;
}
