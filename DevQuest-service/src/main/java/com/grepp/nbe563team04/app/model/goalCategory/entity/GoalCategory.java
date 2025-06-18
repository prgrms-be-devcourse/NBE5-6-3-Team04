package com.grepp.nbe563team04.app.model.goalCategory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goal_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "korean_name")
    private String koreanName;

    @Column(nullable = false, length = 7)
    private String color;
}
