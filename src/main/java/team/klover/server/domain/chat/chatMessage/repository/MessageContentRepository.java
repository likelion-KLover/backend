package team.klover.server.domain.chat.chatMessage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import team.klover.server.domain.chat.chatMessage.entity.MessageContent;

import java.util.List;

public interface MessageContentRepository extends MongoRepository<MessageContent, String> {
    List<MessageContent> findByIdIn(List<String> messageIds);
}
