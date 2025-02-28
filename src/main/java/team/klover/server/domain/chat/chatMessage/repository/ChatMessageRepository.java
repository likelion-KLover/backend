package team.klover.server.domain.chat.chatMessage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.chat.chatMessage.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 해당 채팅방의 메시지 조회
    Page<ChatMessage> findByChatRoomId(Long chatRoomId, Pageable pageable);

    // 해당 채팅방에서 메시지 검색(닉네임/내용)
    @Query("SELECT cm FROM ChatMessage cm " +
            "JOIN cm.member m " +
            "WHERE cm.chatRoom.id = :chatRoomId " +
            "AND (cm.content LIKE %:keyword% OR m.nickname LIKE %:keyword%)")
    Page<ChatMessage> searchByKeyword(@Param("chatRoomId") Long chatRoomId,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);
}
