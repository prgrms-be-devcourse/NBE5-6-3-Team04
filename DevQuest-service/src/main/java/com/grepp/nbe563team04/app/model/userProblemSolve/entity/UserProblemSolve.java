package com.grepp.nbe563team04.app.model.userProblemSolve.entity;

import com.grepp.nbe563team04.app.model.member.entity.Member;
import com.grepp.nbe563team04.app.model.problem.entity.Problem;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "user_problem_solve")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UserProblemSolve {

    @EmbeddedId
    private UserProblemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Member userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("problemId")
    @JoinColumn(name = "problem_id")
    private Problem problemId;

    @Column(name = "solve_count", nullable = false)
    private int solveCount;
}

