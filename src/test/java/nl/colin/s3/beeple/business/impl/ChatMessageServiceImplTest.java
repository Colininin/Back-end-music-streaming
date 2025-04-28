package nl.colin.s3.beeple.business.impl;

import jakarta.transaction.Transactional;
import nl.colin.s3.beeple.controller.dto.ChatMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;

import static org.mockito.Mockito.*;
@Transactional
class ChatMessageServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    @Test
    void testSaveMessage_success() {
        MockitoAnnotations.openMocks(this);

        ChatMessage message = new ChatMessage();
        message.setUsername("testUser");
        message.setText("Helloooo World!");
        message.setTimestamp("2025-1-01T12:00:00");

        when(redisTemplate.opsForList()).thenReturn(listOperations);

        chatMessageService.saveMessage(message);

        verify(listOperations, times(1)).rightPush("chatMessage", message);
        verify(redisTemplate, times(1)).expire("chatMessage", Duration.ofHours(3));
    }


    @Test
    void testGetMessages_success() {
        MockitoAnnotations.openMocks(this);

        ChatMessage message1 = new ChatMessage();
        message1.setUsername("user1");
        message1.setText("Hello");
        message1.setTimestamp("2025-1-01T12:00:00");

        ChatMessage message2 = new ChatMessage();
        message2.setUsername("user2");
        message2.setText("Hi");
        message2.setTimestamp("2025-1-01T12:05:00");

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range("chatMessage", 0, -1)).thenReturn(List.of(message1, message2));

        List<Object> messages = chatMessageService.getMessages();

        verify(redisTemplate, times(1)).opsForList();
        verify(listOperations, times(1)).range("chatMessage", 0, -1);
        assert messages.size() == 2;
        assert messages.contains(message1);
        assert messages.contains(message2);
    }
}