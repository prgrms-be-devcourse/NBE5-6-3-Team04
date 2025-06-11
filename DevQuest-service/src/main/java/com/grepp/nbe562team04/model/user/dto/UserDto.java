package com.grepp.nbe562team04.model.user.dto;

import com.grepp.nbe562team04.model.auth.code.Role;
import com.grepp.nbe562team04.model.level.entity.Level;
import com.grepp.nbe562team04.model.user.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 받는 생성자
public class UserDto {
    private Long userId;
    private String email;
    private String password;
    private Role role;
    private String nickname;
    private String userImage;
    private Level level;
    private Integer exp;
    private String comment;
    private LocalDate createdAt;
    private LocalDate deletedAt;

    private MultipartFile userImageFile;

    public UserDto (User user) {
        this.userId = user.getUserId();
        this.role = user.getRole();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.level = user.getLevel();
        this.comment = user.getComment();
        this.createdAt = user.getCreatedAt();
        this.deletedAt = user.getDeletedAt();
    }


}