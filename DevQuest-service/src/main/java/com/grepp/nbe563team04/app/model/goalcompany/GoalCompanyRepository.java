package com.grepp.nbe563team04.app.model.goalcompany;

import com.grepp.nbe563team04.app.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe563team04.app.model.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCompanyRepository extends JpaRepository<GoalCompany, Long> {
//    기본적인 CRUD
//    findById(id)	    SELECT * FROM table WHERE id = ? // 테이블 명은 만들어둔 entity 값을 보고 판단, id는 엔티티에서의 기본키를 의미
//    findAll()	        SELECT * FROM table
//    save(entity)	    INSERT 또는 UPDATE
//    deleteById(id)	DELETE FROM table WHERE id = ?
//    existsById(id)	존재 여부 확인


    // 알림 생성 시 유저의 모든 목표 기업 조회
    List<GoalCompany> findAllByMember(Member member);

    // 연결된 기업이 있는지 확인 후 삭제
    List<GoalCompany> findByNormalizedCompanyId(Long normalizedCompanyId);
}