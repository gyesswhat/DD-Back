package com.example.ddback.user;

import com.example.ddback.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String userId;
    @Column
    private String nickname;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private Integer age;
    @Column
    private String gender;
    @Column
    private String partnerGender;
    @Column
    private Integer veganState;
    @Convert(converter = StringListConverter.class)
    private List<String> hobby;
    @Convert(converter = StringListConverter.class)
    private List<String> location;
    @Convert(converter = StringListConverter.class)
    private List<String[]> partnerLocation;
    @Column
    private String imageLink;
}
