package com.example.ddback.swipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikematchRepository extends JpaRepository<Likematch, Integer> {
    @Query(nativeQuery = true, value="SELECT * FROM likematch where from_user_id=:toUserId AND to_user_id=:fromUserId")
    Likematch findPartnerById(@Param("fromUserId") String fromUserId, @Param("toUserId") String toUserId);
}
