package com.grepp.nbe563team04.app.model.member;

import com.grepp.nbe563team04.app.model.member.entity.Member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByUserId(Long userId);

    Boolean existsByEmail(String email);

    List<Long> findWithAchievedIdsByUserId(Long userId);

    long countByDeletedAtIsNull();

    List<Member> findAllByDeletedAtIsNull();

    List<Member> findTop3ByOrderByLevelDesc();
}