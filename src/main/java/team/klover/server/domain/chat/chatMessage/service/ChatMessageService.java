package team.klover.server.domain.chat.chatMessage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.chat.chatMessage.dto.res.ChatMessageDto;

public interface ChatMessageService {
    // 채팅 메시지 조회
    Page<ChatMessageDto> findByChatRoomId(Long currentMemberId, Long chatRoomId, Pageable pageable);
}
