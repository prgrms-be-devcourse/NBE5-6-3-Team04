package com.grepp.nbe563team04.model.proplem.entity;

import com.grepp.nbe563team04.model.goalCategory.entity.GoalCategory;
import com.grepp.nbe563team04.model.goalcompany.entity.GoalCompany;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "problem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String site; // 예: "프로그래머스", "백준"

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String url;

    private Integer level; // 난이도 (nullable 가능)

    @Column(columnDefinition = "TEXT")
    private String tags; // 쉼표 구분: "BFS,그래프"

    private String tier; // 예: "Level 2", "Gold IV"

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist // 엔티티가 처음 저장될때 실행
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate // 엔티티가 수정될 때 실행
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}