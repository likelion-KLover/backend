package team.klover.server.domain.chat.chatRoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomMember;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    // 해당 채팅방에 실시간 참여중이 아닌 인원수
    @Query("SELECT COUNT(cm) FROM ChatRoomMember cm WHERE cm.chatRoom.id = :chatRoomId AND cm.lastReadMessageId IS NULL")
    Long countInActiveMembers(@Param("chatRoomId") Long chatRoomId);

    // memberId, chatRoomID로 ChatRoomMember 찾기
    ChatRoomMember findByMemberIdAndChatRoomId(Long currentMemberId, Long chatRoomId);
}
