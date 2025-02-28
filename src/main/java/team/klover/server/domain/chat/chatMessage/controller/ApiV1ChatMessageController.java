package team.klover.server.domain.chat.chatMessage.controller;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.chat.chatMessage.dto.res.ChatMessageDto;
import team.klover.server.domain.chat.chatMessage.service.ChatMessageService;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomPage;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.util.AuthUtil;

@RestController
@RequestMapping("/api/v1/chat-room")
@RequiredArgsConstructor
public class ApiV1ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 해당 채팅방의 메시지 조회
    @GetMapping("/{chatRoomId}")
    public ApiResponse<ChatMessageDto> findByChatRoomId(@ModelAttribute ChatRoomPage request, @PathVariable("chatRoomId") Long chatRoomId) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(chatMessageService.findByChatRoomId(currentMemberId, chatRoomId, pageable)));
    }

//    // 해당 채팅방에서 메시지 검색(내용/닉네임)
//    @GetMapping("/{chatRoomId}")
//    public
//
//    // 해당 채팅방에서 메시지 생성
//    @PostMapping("/{chatRoomId}")
//
//    // 해당 메시지 삭제
//    @DeleteMapping("/{messageId}")
}
