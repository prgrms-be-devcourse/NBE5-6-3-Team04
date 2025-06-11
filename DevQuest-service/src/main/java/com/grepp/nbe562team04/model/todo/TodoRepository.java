package com.grepp.nbe562team04.model.todo;

import com.grepp.nbe562team04.model.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByGoalGoalId(Long goalId); // 특정 목표(goal)에 속한 투두 리스트

    void deleteByGoalGoalId(Long goalId);
    long countByGoal_Company_User_UserIdAndIsDoneTrue(Long userId);

}