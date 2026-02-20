package com.network.protocols.service;

import com.network.protocols.model.ChatMessage;
import com.network.protocols.model.ChatMessageEntity;
import com.network.protocols.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository repository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public void saveMessage(ChatMessage chatMessage) {
        ChatMessageEntity entity = new ChatMessageEntity(
                chatMessage.getSender(),
                chatMessage.getContent(),
                chatMessage.getType().name());
        ChatMessageEntity saved = repository.save(entity);
        // Include the actual timestamp on the DTO for the response
        chatMessage.setTimestamp(saved.getTimestamp().format(FORMATTER));
    }

    public List<ChatMessage> getChatHistory() {
        return repository.findAllByOrderByTimestampAsc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ChatMessage mapToDto(ChatMessageEntity entity) {
        ChatMessage dto = new ChatMessage();
        dto.setSender(entity.getSender());
        dto.setContent(entity.getContent());
        dto.setType(ChatMessage.MessageType.valueOf(entity.getType()));
        dto.setTimestamp(entity.getTimestamp().format(FORMATTER));
        return dto;
    }
}
