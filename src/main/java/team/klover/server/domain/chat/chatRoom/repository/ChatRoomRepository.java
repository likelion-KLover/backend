package team.klover.server.domain.chat.chatRoom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;
import team.klover.server.domain.member.v1.entity.Member;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 사용자가 참여 중인 채팅방 목록 조회
    Page<ChatRoom> findByChatRoomMembers_Member(Member member, Pageable pageable);
}
