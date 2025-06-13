package com.grepp.nbe563team04.model.user;

import com.grepp.nbe563team04.model.user.entity.User;
import com.grepp.nbe563team04.model.user.entity.UserInterest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    @Query("SELECT ui FROM UserInterest ui WHERE ui.user = :user")
    List<UserInterest> findByUser(@Param("user") User user);

    @Query("SELECT ui.interest.interestName FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.interest.type = 'ROLE'")
    List<String> findTop6ByUserIdAndType(@Param("userId") String userId);
}
