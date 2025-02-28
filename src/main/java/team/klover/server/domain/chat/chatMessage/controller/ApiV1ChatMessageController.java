package team.klover.server.domain.chat.chatMessage.controller;

import jakarta.validation.Valid;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.chat.chatMessage.dto.req.ChatMessageForm;
import team.klover.server.domain.chat.chatMessage.dto.res.ChatMessageDto;
import team.klover.server.domain.chat.chatMessage.entity.ChatMessagePage;
import team.klover.server.domain.chat.chatMessage.service.ChatMessageService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

@RestController
@RequestMapping("/api/v1/chat-room/message")
@RequiredArgsConstructor
public class ApiV1ChatMessageController {
    private final ChatMessageService chatMessageService;

    // 해당 채팅방의 메시지 조회
    @GetMapping("/{chatRoomId}")
    public ApiResponse<ChatMessageDto> findByChatRoomId(@ModelAttribute ChatMessagePage request, @PathVariable("chatRoomId") Long chatRoomId) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(chatMessageService.findByChatRoomId(currentMemberId, chatRoomId, pageable)));
    }

    // 해당 채팅방에서 메시지 검색(닉네임/내용)
    @GetMapping("/{chatRoomId}/keyword")
    public ApiResponse<ChatMessageDto> searchChatMessage(@ModelAttribute ChatMessagePage request, @PathVariable("chatRoomId") Long chatRoomId,
                                                         @RequestParam("keyword") String keyword) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(chatMessageService.searchByKeyword(currentMemberId, chatRoomId, keyword, pageable)));
    }

    // 해당 채팅방에서 메시지 생성
    @PostMapping("/{chatRoomId}")
    public ApiResponse<String> writeChatMessage(@PathVariable("chatRoomId") Long chatRoomId, @RequestBody @Valid ChatMessageForm chatMessageForm){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatMessageService.writeChatMessage(currentMemberId, chatRoomId, chatMessageForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 해당 메시지 삭제
    @DeleteMapping("/{messageId}")
    public ApiResponse<String> deleteChatMessage(@PathVariable("messageId") Long messageId){
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatMessageService.deleteChatMessage(currentMemberId, messageId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
