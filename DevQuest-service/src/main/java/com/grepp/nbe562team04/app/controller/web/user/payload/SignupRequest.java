package com.grepp.nbe562team04.app.controller.web.user.payload;

import com.grepp.nbe562team04.model.user.dto.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    private String nickname;
    @NotBlank
    @Email
    private String email;
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "비밀번호는 8자 이상, 영문+숫자를 포함해야 합니다."
    )
    private String password;

    public UserDto toDto() {
        UserDto userDto = new UserDto();
        userDto.setNickname(nickname);
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setCreatedAt(LocalDate.now());
        return userDto;
    }
}