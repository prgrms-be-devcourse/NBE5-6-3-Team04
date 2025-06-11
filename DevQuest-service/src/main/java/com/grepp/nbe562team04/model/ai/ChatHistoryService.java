package com.grepp.nbe562team04.model.ai;


import com.grepp.nbe562team04.model.ai.dto.ChatMessageDto;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatHistoryService {

    public List<ChatMessageDto> saveMessage(HttpSession session, ChatMessageDto message) {


          List<ChatMessageDto> history = (List<ChatMessageDto>) session.getAttribute("chatHistory");



        if (ObjectUtils.isEmpty(history)) {
            history = new ArrayList<>();
        }

        history.add(message);

        session.setAttribute("chatHistory", history);

        System.out.println("대화 시작");
        System.out.println("====================================================");


        for (ChatMessageDto msg : history) {
            System.out.println("[" + msg.getRole() + "] " + msg.getMessage());
        }

        return history;
    }
}
