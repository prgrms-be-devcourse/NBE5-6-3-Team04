package com.grepp.nbe563team04.infra.feign.client;

import com.grepp.nbe563team04.app.model.member.dto.SmtpDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "DevQuest-mail",
    url = "${mail.service-url}"
)
public interface MailApi {

    @PostMapping("/mail/send")
    void sendMail(
        @RequestHeader(name = "x-member-id", required = false) String userId,
        @RequestHeader(name = "x-member-role", required = false) String role,
        @RequestBody SmtpDto payload
    );
}
