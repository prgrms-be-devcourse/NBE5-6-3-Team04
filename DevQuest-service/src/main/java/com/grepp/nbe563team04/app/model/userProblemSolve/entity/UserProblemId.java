package com.grepp.nbe563team04.app.model.userProblemSolve.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserProblemId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "problem_id")
    private Long problemId;

}
