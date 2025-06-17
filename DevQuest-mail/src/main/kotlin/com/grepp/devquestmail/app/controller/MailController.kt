package com.grepp.devquestmail.app.controller

import com.grepp.devquestmail.app.controller.payload.SmtpRequest
import com.grepp.devquestmail.app.model.MailService
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("mail")
@PreAuthorize("hasRole('SERVER')") // 일반 사용자는 접근 불가, 내부 사용자들만 서비스 권한 있음
class MailController(
    private val mailService: MailService
) {

    @PostMapping("send")
    fun send(
        @RequestBody
        @Valid
        request: SmtpRequest
    ){
        mailService.send(request)
    }
}