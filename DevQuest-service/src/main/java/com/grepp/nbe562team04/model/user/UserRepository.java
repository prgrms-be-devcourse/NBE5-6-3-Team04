package com.grepp.nbe562team04.model.user;

import com.grepp.nbe562team04.model.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

//    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userInterests WHERE u.userId = :id")
//    Optional<User> findUserWithUserInterestsById(Long id);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByUserId(Long userId);

    Boolean existsByEmail(String email);
}