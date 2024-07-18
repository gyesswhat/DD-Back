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
public class Likematch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likematchId;
    @Column
    private String fromUserId;
    @Column
    private String toUserId;
}
