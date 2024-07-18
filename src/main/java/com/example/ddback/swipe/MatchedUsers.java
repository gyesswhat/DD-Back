package com.example.ddback.swipe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MatchedUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchedUsersId;
    @Column
    private String user1Id;
    @Column
    private String user2Id;
    @Column
    private String matchedTime;
}
