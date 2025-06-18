package com.grepp.nbe563team04.model.member.dto;

import com.grepp.nbe563team04.model.auth.code.Role;
import com.grepp.nbe563team04.model.level.entity.Level;
import com.grepp.nbe563team04.model.member.entity.Member;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 받는 생성자
public class MemberDto {

    private Long userId;
    private String email;
    private String password;
    private Role role;
    private String nickname;
    private Level level;
    private Integer exp;
    private String comment;
    private LocalDate createdAt;
    private LocalDate deletedAt;
    private String profileImageUrl;


    public MemberDto(Member member) {
        this.userId = member.getUserId();
        this.role = member.getRole();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.level = member.getLevel();
        this.comment = member.getComment();
        this.createdAt = member.getCreatedAt();
        this.deletedAt = member.getDeletedAt();
    }

    // admin-dashboard 내 Top5 Member 이미지 표시
    public static MemberDto from(Member member) {
        MemberDto dto = new MemberDto();
        dto.userId = member.getUserId();
        dto.nickname = member.getNickname();
        dto.level = member.getLevel();
        dto.profileImageUrl = "/admin/dashboard/image/" + member.getUserId();
        return dto;
    }

}