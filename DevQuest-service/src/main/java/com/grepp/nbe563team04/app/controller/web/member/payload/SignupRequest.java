package com.grepp.nbe563team04.app.controller.web.member.payload;

import com.grepp.nbe563team04.model.member.dto.MemberDto;
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

    public MemberDto toDto() {
        MemberDto memberDto = new MemberDto();
        memberDto.setNickname(nickname);
        memberDto.setEmail(email);
        memberDto.setPassword(password);
        memberDto.setCreatedAt(LocalDate.now());
        return memberDto;
    }
}