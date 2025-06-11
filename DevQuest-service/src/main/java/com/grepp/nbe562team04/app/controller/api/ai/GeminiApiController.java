package com.grepp.nbe562team04.app.controller.api.ai;

import com.grepp.nbe562team04.model.ai.dto.ChatMessageDto;
import com.grepp.nbe562team04.model.ai.dto.GeminiInputDto;
import com.grepp.nbe562team04.model.ai.dto.Role;
import com.grepp.nbe562team04.model.ai.ChatHistoryService;
import com.grepp.nbe562team04.model.ai.GeminiService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class GeminiApiController {

    private final GeminiService geminiService;
    private final ChatHistoryService chatHistoryService;

    @PostMapping("/feedback")
    public ResponseEntity<Map<String, String>> getGeminiReply(
        @RequestBody GeminiInputDto inputDto,
        HttpSession session
    ) {
        String userMessage = inputDto.getPrompt();

        // 세션에 사용자 메시지 저장
        List<ChatMessageDto> history = chatHistoryService.saveMessage(
            session,
            ChatMessageDto.builder()
                .role(Role.USER)
                .message(userMessage)
                .build()
        );

        // Gemini 응답 받기
        String aiReply = geminiService.getGeminiReply(history);

        // 응답 메시지도 세션에 저장
        chatHistoryService.saveMessage(
            session,
            ChatMessageDto.builder()
                .role(Role.MODEL)
                .message(aiReply)
                .build()
        );

        // 응답 JSON 반환
        return ResponseEntity.ok(Map.of("reply", aiReply));
    }
}