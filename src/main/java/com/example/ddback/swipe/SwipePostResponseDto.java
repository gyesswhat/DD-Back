package com.example.ddback.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SwipePostResponseDto {
    //[
    //	{
    //		"partnerId": String,
    //		"nickname": String,
    //		"name": String,
    //		"age": String,
    //		"gender": String,
    //		"imageLink": String,
    //		"location2": String
    //	}
    //]

    private String partnerId;
    private String nickname;
    private Integer age;
    private String gender;
    private String imageLink;
    private String location2;
}
