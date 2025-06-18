package com.grepp.nbe563team04.app.model.todo.entity;

import com.grepp.nbe563team04.app.model.goal.entity.Goal;
import com.grepp.nbe563team04.app.model.problem.entity.Problem;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long todoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    private String content;

    private String url;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_done")
    private Boolean isDone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem; // 어떤 문제로 생성된 투두인지 (nullable 가능)

    @Column(name = "source_type")
    private String sourceType; // 예: "PROBLEM_RECOMMEND", "USER"

}
