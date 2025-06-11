package com.grepp.nbe562team04.model.user.entity;

import com.grepp.nbe562team04.model.auth.code.Role;
import com.grepp.nbe562team04.model.goalcompany.entity.GoalCompany;
import com.grepp.nbe562team04.model.interest.entity.Interest;
import com.grepp.nbe562team04.model.level.entity.Level;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String profileImageUrl;
    private String nickname;
    @Column(name = "user_image")
    private String userImage = "default.png";
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;
    private Integer exp;
    private String comment;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInterest> userInterests;

    @Column(name = "is_notification_on")
    private boolean NotificationOn;

    private LocalDate createdAt;
    @Column(name = "deleted_at")
    private LocalDate deletedAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalCompany> goalCompanies;

    // 경험치 추가 로직
    public void addXp(int amount) {
        if (this.exp == null) this.exp = 0; // null 방어
        this.exp += amount;
    }
}