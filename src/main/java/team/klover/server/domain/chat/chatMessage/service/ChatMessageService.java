package team.klover.server.domain.chat.chatMessage.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.klover.server.domain.chat.chatMessage.dto.req.ChatMessageForm;
import team.klover.server.domain.chat.chatMessage.dto.res.ChatMessageDto;

public interface ChatMessageService {
    // 해당 채팅방의 메시지 조회
    Page<ChatMessageDto> findByChatRoomId(Long currentMemberId, Long chatRoomId, Pageable pageable);

    // 해당 채팅방에서 메시지 검색(닉네임/내용)
    Page<ChatMessageDto> searchByKeyword(Long currentMemberId, Long chatRoomId, String keyword, Pageable pageable);

    // 해당 채팅방에서 메시지 생성
    void writeChatMessage(Long currentMemberId, Long chatRoomId, @Valid ChatMessageForm chatMessageForm);

    // 해당 메시지 삭제
    void deleteChatMessage(Long currentMemberId, Long messageId);

    // 해당 채팅방의 모든 메시지 삭제
    void deleteAllChatMessages(Long chatRoomId);
}
