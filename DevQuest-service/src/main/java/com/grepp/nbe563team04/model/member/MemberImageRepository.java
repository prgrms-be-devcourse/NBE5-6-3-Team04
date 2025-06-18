package com.grepp.nbe563team04.model.member;

import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.entity.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
    Optional<MemberImage> findTopByMemberAndActivatedOrderByCreatedAtDesc(Member member, Boolean activated);

    List<MemberImage> findByMemberAndActivatedTrue(Member member);

    Optional<MemberImage> findTopByMemberAndActivatedTrueOrderByCreatedAtDesc(Member member);
}
