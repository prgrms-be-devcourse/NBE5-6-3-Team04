package com.grepp.nbe563team04.model.goalCategory;

import com.grepp.nbe563team04.model.goalCategory.entity.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoalCategoryRepository extends JpaRepository<GoalCategory, Long> {
//    기본적인 CRUD
//    findById(id)	    SELECT * FROM table WHERE id = ? // 테이블 명은 만들어둔 entity 값을 보고 판단, id는 엔티티에서의 기본키를 의미
//    findAll()	        SELECT * FROM table
//    save(entity)	    INSERT 또는 UPDATE
//    deleteById(id)	DELETE FROM table WHERE id = ?
//    existsById(id)	존재 여부 확인

    Optional<GoalCategory> findByCategoryName(String categoryName);
}
