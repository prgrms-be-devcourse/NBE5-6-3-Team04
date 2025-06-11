package com.grepp.nbe562team04.model.user;

import com.grepp.nbe562team04.model.user.entity.User;
import com.grepp.nbe562team04.model.user.entity.UserInterest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    @Query("SELECT ui FROM UserInterest ui WHERE ui.user = :user")
    List<UserInterest> findByUser(@Param("user") User user);
}
