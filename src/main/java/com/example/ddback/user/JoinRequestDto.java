package com.example.ddback.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JoinRequestDto {
    // "userId": String,
    //"password": String,
    //"name": String,
    //"age": String,
    //"gender": String, // 내 성별 ("여성"/"남성"/"선택안함")
    //"partnerGender": String, // 상대방 성별 ("여성"/"남성"/"선택안함")
    //"hobby": String Array
    //"location": String array, // 내 위치, 1개 | [location1, location2]
    //"partnerLocation": String array // 상대방 위치, 3개 | partnerLocation1[location1, location2]

    private String userId;
    private String nickname;
    private String password;
    private String name;
    private Integer age;
    private String gender;
    private String partnerGender;
    private Integer veganState;
    private ArrayList<String> hobby;
    private ArrayList<String> location;
    private ArrayList<String[]> partnerLocation;
}
