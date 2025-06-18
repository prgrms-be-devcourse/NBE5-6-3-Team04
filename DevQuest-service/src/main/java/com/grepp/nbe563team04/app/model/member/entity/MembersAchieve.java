package com.grepp.nbe563team04.app.model.member.entity;

import com.grepp.nbe563team04.app.model.achievement.entity.Achievement;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersAchieve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achieve_id")
    private Achievement achievement;

    @Column(name = "achieved_at")
    private LocalDateTime achievedAt;
}
