package com.example.ddback.swipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SwipePostResponseDto {
    //[
    //	{
    //		"partnerId": String,
    //		"nickname": String,
    //		"age": String,
    //		"veganState": Integer,
    //		"hobby": String array,
    //		"imageLink": String,
    //		"location2": String
    //	}
    //]

    private String partnerId;
    private String nickname;
    private Integer age;
    private Integer veganState;
    private List<String> hobby;
    private String imageLink;
    private String location2;
}
