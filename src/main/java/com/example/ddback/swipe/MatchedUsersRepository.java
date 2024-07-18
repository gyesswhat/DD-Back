package com.example.ddback.swipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface MatchedUsersRepository extends JpaRepository<MatchedUsers, Integer> {
    @Query(nativeQuery = true, value = "SELECT*FROM matched_users WHERE user1id=:userId OR user2id=:userId")
    ArrayList<MatchedUsers> findAllByUserId(@Param("userId") String userId);
}
