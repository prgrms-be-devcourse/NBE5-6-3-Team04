package com.grepp.nbe562team04.app.controller.api.user;

import com.grepp.nbe562team04.infra.response.ApiResponse;
import com.grepp.nbe562team04.model.user.UserService;
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
@RequestMapping("api/user")
public class UserApiController {

    private final UserService userService;

    @GetMapping("exists/{email}")
    public ResponseEntity<ApiResponse<Boolean>> existsEmail(@PathVariable String email){
        return ResponseEntity.ok(ApiResponse.success(
            userService.isDuplicatedEmail(email)
        ));
    }
}
