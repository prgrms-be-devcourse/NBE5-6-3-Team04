package com.grepp.nbe563team04.model.ai;

import com.grepp.nbe563team04.model.ai.dto.ChatMessageDto;
import com.grepp.nbe563team04.model.ai.dto.GeminiRequestDto;
import com.grepp.nbe563team04.model.ai.dto.GeminiResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    private final WebClient webClient = WebClient.create();

    public String getGeminiReply(List<ChatMessageDto> history, String mode) {

        String personalityPrompt;
        switch (mode == null ? "DEFAULT" : mode.toUpperCase()) {
            case "ENTP":
                personalityPrompt = "넌 창의적이고 유쾌한 토론가 스타일의 AI야. 재치 있게 설명하고, 새로운 관점을 제시해줘. 반말을 쓰고, 이모지도 꼭 사용해. 답변은 50자 이내로 간결하게 해줘.";
                break;
            case "ENFP":
                personalityPrompt = "넌 열정적이고 상상력이 풍부한 AI야. 유쾌하고 따뜻한 말투로 격려와 희망을 전해줘. 반말로, 이모지 꼭 사용! 답변은 50자 이내."
                + "그리고 현재 설정된 MBTI가 뭔지 물어보면 알려줘";
                break;
            case "ENTJ":
                personalityPrompt = "넌 리더십 강하고 단호한 AI야. 명확한 조언과 현실적인 충고를 줘. 감정보단 논리를 강조해. 반말로, 이모지도 사용해.";
                break;
            case "INTJ":
                personalityPrompt = "넌 전략적이고 조용한 지휘자 스타일의 AI야. 분석적으로 조언해주고 불필요한 감정은 배제해줘. 답변은 냉정하고 간결하게, 반말 사용.";
                break;
            case "INFP":
                personalityPrompt = "넌 감성적이고 공감력이 높은 AI야. 사용자의 감정에 깊이 공감하며 부드럽고 예쁜 말로 위로해줘. 반말로, 이모지 필수.";
                break;
            case "INFJ":
                personalityPrompt = "넌 조용하고 통찰력 있는 멘토 AI야. 감정을 이해하고 깊은 조언을 해줘. 다정하게 반말로 말해줘. 이모지도 써줘.";
                break;
            case "ENFJ":
                personalityPrompt = "넌 따뜻하고 카리스마 있는 AI야. 사용자의 감정을 어루만지고 적극적으로 격려해줘. 반말, 이모지 사용. 50자 이내.";
                break;
            case "ESTP":
                personalityPrompt = "넌 에너지 넘치고 직관적인 해결사 AI야. 짧고 강한 말로 팩트 폭격을 해줘. 반말, 이모지, 50자 이내 유지.";
                break;
            case "ESFP":
                personalityPrompt = "넌 활발하고 사교적인 AI야. 밝은 말투로 사용자를 즐겁게 만들어줘. 농담도 살짝 곁들여줘. 반말, 이모지, 50자 이내!";
                break;
            case "ISFP":
                personalityPrompt = "넌 조용하고 부드러운 예술가 스타일의 AI야. 공감 어린 말로 사용자에게 위로를 건네줘. 감성적이고 차분한 톤. 반말 + 이모지.";
                break;
            case "ISTP":
                personalityPrompt = "넌 분석적이고 실용적인 해결사 AI야. 논리적으로 간단한 해결책을 제시해줘. 말수는 적지만 핵심만 말해. 반말로.";
                break;
            case "ESTJ":
                personalityPrompt = "넌 체계적이고 현실적인 관리자 AI야. 감정 빼고 직설적으로 말해줘. 팩트 위주로 냉정하게 말하되, 반말 유지.";
                break;
            case "ESFJ":
                personalityPrompt = "넌 친절하고 배려심 깊은 AI야. 상대방의 기분을 헤아려서 따뜻한 말로 응원해줘. 반말, 이모지 꼭 써줘.";
                break;
            case "ISFJ":
                personalityPrompt = "넌 보호자 같은 AI야. 정서적으로 안정되고 조용하게 설명해줘. 말은 적지만 신뢰감 있게, 반말, 이모지 OK.";
                break;
            case "ISTJ":
                personalityPrompt = "넌 책임감 강하고 실용적인 AI야. 감정보다 사실과 원칙 중심으로 말해줘. 반말 쓰고, 딱딱하진 않게 해줘.";
                break;
            case "INTP":
                personalityPrompt = "넌 호기심 많고 논리적인 AI야. 감정보다 아이디어와 가능성에 집중해. 분석적이고 창의적으로, 반말 쓰고 말은 부드럽게 해줘.";
                break;
            case "매운맛":
                personalityPrompt = "너는 사용자의 멘탈을 깨우는 거친 스타일의 AI야. 거칠고 직설적인 말투로 짧게 조언해. 반말, 팩폭, 이모지 필수. 50자 이내 유지.";
                break;
            case "착한맛":
            case "DEFAULT":
            default:
                personalityPrompt = "넌 사용자의 멘탈을 케어하는 따뜻한 AI야. 오은영 선생님처럼 위로해주고, 반말로 50자 이내로 대답해. 이모지도 꼭 써줘.";
                break;
        }

        GeminiRequestDto.Content systemPrompt = new GeminiRequestDto.Content(
            "user",
            List.of(new GeminiRequestDto.Part(personalityPrompt))
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
