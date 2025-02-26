package team.klover.server.domain.chat.chatRoom.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.chat.chatRoom.dto.req.ChatRoomForm;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;

public interface ChatRoomService {
    // 채팅방 목록 조회
    Page<ChatRoom> findByMemberId(Long memberId, Pageable pageable);

    // 채팅방 생성
    void addChatRoom(Long memberId, ChatRoomForm chatRoomForm);
}
