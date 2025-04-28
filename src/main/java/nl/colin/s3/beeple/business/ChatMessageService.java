package nl.colin.s3.beeple.business;

import nl.colin.s3.beeple.controller.dto.ChatMessage;
import java.util.List;

public interface ChatMessageService {
    void saveMessage(ChatMessage message);
    List<Object> getMessages();
}
