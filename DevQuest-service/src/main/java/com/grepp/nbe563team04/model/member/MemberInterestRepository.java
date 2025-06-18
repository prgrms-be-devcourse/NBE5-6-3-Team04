package com.grepp.nbe563team04.model.member;

import com.grepp.nbe563team04.model.member.entity.MemberInterest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {

    @Query("SELECT mi.interest.interestName FROM MemberInterest mi WHERE mi.member.userId = :userId AND mi.interest.type = 'ROLE'")
    List<String> findTop5ByUserIdAndType(@Param("userId") Long userId);

    @Query("SELECT COUNT(mi) FROM MemberInterest mi WHERE mi.interest.interestName = :interestName AND mi.interest.type = 'SKILL'")
    Integer countByInterestName(@Param("interestName") String interestName);

    @Query("SELECT mi.interest.interestName FROM MemberInterest mi WHERE mi.member.userId = :userId AND mi.interest.type = 'SKILL'")
    List<String> findTop5SkillsByUserId(@Param("userId") Long userId);

    /**
     * 모든 활성 회원의 언어 관심도를 한 번에 조회 (N+1 문제 해결)
     * @return 모든 활성 회원의 언어 관심도 목록
     */
    @Query("SELECT mi.interest.interestName FROM MemberInterest mi " +
           "JOIN mi.member m " +
           "WHERE mi.interest.type = 'SKILL' " +
           "AND m.deletedAt IS NULL")
    List<String> findAllActiveMemberTopLangs();
}
