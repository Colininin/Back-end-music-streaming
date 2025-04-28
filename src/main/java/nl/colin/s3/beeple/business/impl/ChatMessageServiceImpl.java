package nl.colin.s3.beeple.business.impl;

import nl.colin.s3.beeple.business.ChatMessageService;
import nl.colin.s3.beeple.controller.dto.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private RedisTemplate<String, Object> redisTemplate;

    public ChatMessageServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String CHAT_MESSAGE_KEY = "chatMessage";

    @Override
    public void saveMessage(ChatMessage message){
        redisTemplate.opsForList().rightPush(CHAT_MESSAGE_KEY, message);
        redisTemplate.expire(CHAT_MESSAGE_KEY, Duration.ofHours(3));
    }

    @Override
    public List<Object> getMessages(){
        return redisTemplate.opsForList().range(CHAT_MESSAGE_KEY, 0, -1);
    }
}
