package com.grepp.nbe563team04.model.user;

import com.grepp.nbe563team04.model.user.dto.UserImageDto;
import com.grepp.nbe563team04.model.user.entity.User;
import com.grepp.nbe563team04.model.user.entity.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findTopByUserAndActivatedOrderByCreatedAtDesc(User user, Boolean activated);

    List<UserImage> findByUserAndActivatedTrue(User user);
}
