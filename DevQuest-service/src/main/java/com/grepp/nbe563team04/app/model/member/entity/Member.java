package com.grepp.nbe563team04.app.model.member.entity;

import com.grepp.nbe563team04.app.model.auth.code.Role;
import com.grepp.nbe563team04.app.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe563team04.app.model.level.entity.Level;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String nickname;
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;
    private Integer exp;
    private String comment;
    @Column(name = "profile_activated")
    private boolean profile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberInterest> memberInterests;

    @Column(name = "is_notification_on")
    private boolean NotificationOn;

    private LocalDate createdAt;
    @Column(name = "deleted_at")
    private LocalDate deletedAt;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalCompany> goalCompanies;

    // 경험치 추가 로직
    public void addXp(int amount) {
        if (this.exp == null) this.exp = 0; // null 방어
        this.exp += amount;
    }

    // admin-dashboard 내 Top5 Member 이미지 표시
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberImage> images;

    public String getActiveProfileImageUrl() {
        if (images == null || images.isEmpty()) {
            return "/img/default-profile.png";
        }

        return images.stream()
            .filter(MemberImage::getActivated)
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .map(img -> img.getSavePath() + "/" + img.getRenameFileName())
            .findFirst()
            .orElse("/img/default-profile.png");
    }
}