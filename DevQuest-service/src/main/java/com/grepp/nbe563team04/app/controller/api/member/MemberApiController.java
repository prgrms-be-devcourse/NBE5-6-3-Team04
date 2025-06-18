package com.grepp.nbe563team04.app.controller.api.member;

import com.grepp.nbe563team04.infra.response.ApiResponse;
import com.grepp.nbe563team04.app.model.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
