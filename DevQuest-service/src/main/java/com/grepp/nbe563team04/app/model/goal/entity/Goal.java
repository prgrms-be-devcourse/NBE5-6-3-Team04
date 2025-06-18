package com.grepp.nbe563team04.app.model.goal.entity;

import com.grepp.nbe563team04.app.model.goalCategory.entity.GoalCategory;
import com.grepp.nbe563team04.app.model.goalcompany.entity.GoalCompany;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "goal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private GoalCompany company;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private GoalCategory category;

    @Column(name = "color")
    private String color;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "is_done", nullable = false)
    private Boolean isDone = false;
}
