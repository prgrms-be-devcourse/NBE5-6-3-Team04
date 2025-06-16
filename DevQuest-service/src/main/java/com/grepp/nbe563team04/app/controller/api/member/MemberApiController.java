package com.grepp.nbe563team04.app.controller.api.member;

import com.grepp.nbe563team04.app.controller.web.member.payload.SignupRequest;
import com.grepp.nbe563team04.infra.response.ApiResponse;
import com.grepp.nbe563team04.model.member.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("exists/{email}")
    public ResponseEntity<ApiResponse<Boolean>> existsEmail(@PathVariable String email){
        return ResponseEntity.ok(ApiResponse.success(
            memberService.isDuplicatedEmail(email)
        ));
    }

    @PostMapping("verify")
    public ResponseEntity<ApiResponse<Void>> verify(
        @Valid @RequestBody SignupRequest request,
        HttpSession session) {

        String token = session.getId();
        session.setAttribute(token, request);
        memberService.verify(token, request);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
