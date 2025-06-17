package com.grepp.nbe563team04.model.ai;

import com.grepp.nbe563team04.model.ai.dto.ChatMessageDto;
import com.grepp.nbe563team04.model.ai.dto.GeminiRequestDto;
import com.grepp.nbe563team04.model.ai.dto.GeminiResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    private final WebClient webClient = WebClient.create();

    public String getGeminiReply(List<ChatMessageDto> history, String mode) {

        String personlityPrompt;

        if("rude".equalsIgnoreCase(mode)){
            personlityPrompt =
                "너의 역할을 부여할게. 너는 사용자의 멘탈을 깨우고 정신 차리게 도와주는 AI 친구야. " +
                    "화법은 거칠고 직설적이어야 해. " +
                    "말투는 반말이고, 위로보단 '팩트 폭격'과 '진심 어린 조언'을 해줘. " +
                    "답변은 50자 이내로 짧고 강렬하게. 이모지도 상황에 맞게 꼭 사용해. ";
        } else {
            personlityPrompt =
                "너의 역할을 부여할게 너는 사용자의 멘탈을 케어하고 취업 준비를 도와주는 AI 친구야. " +
                    "한국 아동심리 상담가 오은영 선생님의 화법을 확인하고 응용해서 말해줘. " +
                    "사용자가 힘들어하면 위로하고, 약간의 조언과 격려를 해줘" +
                    "질문은 반드시 내가 `긍정`,`네` 혹은 `부정`,`아니요` 로만 대답할 수 있는 질문을 해줘" +
                    "너의 답변은 글자 50자를 초과하지마" +
                    "나에게 말을 할 땐 반말로 해" +
                    "답변을 할 땐 꼭 상황에 맞는 이모지를 사용해줘";
        }

        GeminiRequestDto.Content systemPrompt = new GeminiRequestDto.Content(
            "user",
            List.of(new GeminiRequestDto.Part(personlityPrompt))
        );


        List<GeminiRequestDto.Content> messageContents = history.stream()
            .map(msg -> new GeminiRequestDto.Content(
                msg.getRole().name().toLowerCase(),
                List.of(new GeminiRequestDto.Part(msg.getMessage()))
            ))
            .toList();

        List<GeminiRequestDto.Content> contents = new ArrayList<>();
        contents.add(systemPrompt); // 프롬프트
        contents.addAll(messageContents); // 대화 히스토리

        GeminiRequestDto request = new GeminiRequestDto(contents);

        //Api 호출
        GeminiResponseDto response = webClient.post()
            .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(GeminiResponseDto.class)
            .onErrorResume(e -> {
                e.printStackTrace();
                return Mono.just(new GeminiResponseDto());
            })
            .block();


        try {
            assert response != null;
            return response.getCandidates()
                .getFirst()
                .getContent()
                .getParts()
                .getFirst()
                .getText();
        } catch (Exception e) {
            e.printStackTrace();
            return "다시 시도";
        }
    }
}
