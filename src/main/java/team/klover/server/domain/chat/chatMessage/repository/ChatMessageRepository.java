package team.klover.server.domain.chat.chatMessage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.chat.chatMessage.entity.ChatMessage;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 해당 채팅방의 메시지 페이지 조회
    Page<ChatMessage> findByChatRoomId(Long chatRoomId, Pageable pageable);

    // 해당 채팅방의 메시지 리스트 조회
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    // 가장 최근 메시지 조회
    ChatMessage findTopByChatRoomIdOrderByIdDesc(Long chatRoomId);
}
