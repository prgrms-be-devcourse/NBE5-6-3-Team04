package com.grepp.nbe563team04.model.member;

import com.grepp.nbe563team04.model.member.entity.Member;
import com.grepp.nbe563team04.model.member.entity.MemberInterest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

    @Query("SELECT mi FROM MemberInterest mi WHERE mi.member = :member")
    List<MemberInterest> findByMember(@Param("member") Member member);
}
