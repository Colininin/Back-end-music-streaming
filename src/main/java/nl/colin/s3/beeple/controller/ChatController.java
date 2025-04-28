package nl.colin.s3.beeple.controller;

import nl.colin.s3.beeple.business.ChatMessageService;
import nl.colin.s3.beeple.controller.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("ws-chat")
public class ChatController {

    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/global")
    public ChatMessage sendMessage(ChatMessage message) {
        chatMessageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/history")
    @SendTo("/topic/global")
    public List<Object> getChatHistory() {
        return chatMessageService.getMessages();
    }
}
