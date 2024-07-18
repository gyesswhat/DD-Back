package com.example.ddback.swipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SwipeController {
    @Autowired
    SwipeService swipeService;

    @PostMapping("/swipe")
    public ResponseEntity<?> swipe(@RequestBody SwipePostRequestDto swipePostRequestDto) {
        ArrayList<SwipePostResponseDto> userList = swipeService.swipe(swipePostRequestDto.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @PostMapping("/swipe/like")
    public ResponseEntity<?> like(@RequestBody SwipeLikeRequestDto swipeLikeRequestDto) {
        Boolean isMatched = swipeService.likematch(swipeLikeRequestDto);
        SwipeLikeResponseDto response = new SwipeLikeResponseDto(isMatched);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/matchs")
    public ResponseEntity<?> showMatchedUsers(@RequestBody MatchRequestDto matchRequestDto) {
        ArrayList<MatchResponseDto> responses = swipeService.showMatchedUsers(matchRequestDto);
        return (responses!=null)?
                ResponseEntity.status(HttpStatus.OK).body(responses):
                ResponseEntity.status(HttpStatus.OK).body("매칭된 유저가 없습니다.");
    }
}
