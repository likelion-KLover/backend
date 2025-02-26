package team.klover.server.domain.chat.chatRoom.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.chat.chatRoom.dto.req.ChatRoomForm;
import team.klover.server.domain.chat.chatRoom.dto.res.ChatRoomDto;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomPage;
import team.klover.server.domain.chat.chatRoom.service.ChatRoomService;
import team.klover.server.global.common.response.ApiResponse;
import team.klover.server.global.common.response.KloverPage;
import team.klover.server.global.exception.ReturnCode;
import team.klover.server.global.util.AuthUtil;

@RestController
@RequestMapping("/api/v1/chat-room")
@RequiredArgsConstructor
public class ApiV1ChatRoomController {
    private final ChatRoomService chatRoomService;

    // 채팅방 목록 조회(DM/그룹)
    @GetMapping
    public ApiResponse<ChatRoomDto> findByMemberId(@ModelAttribute ChatRoomPage request) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ApiResponse.of(KloverPage.of(chatRoomService.findByMemberId(currentMemberId, pageable)));
    }

    // 채팅방 생성(DM/그룹)
    @PostMapping
    public ApiResponse<String> addChatRoom(@RequestBody @Valid ChatRoomForm chatRoomForm) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatRoomService.addChatRoom(currentMemberId, chatRoomForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 채팅방 이름 수정(그룹)
    @PutMapping("/title/{chatRoomId}")
    public ApiResponse<String> updateChatRoomTitle(@PathVariable Long chatRoomId, @RequestBody @Valid ChatRoomForm chatRoomForm) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatRoomService.updateChatRoomTitle(chatRoomId, currentMemberId, chatRoomForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 채팅방에 초대(그룹)
    @PutMapping("/group/{chatRoomId}")
    public ApiResponse<String> inviteChatRoomMember(@PathVariable Long chatRoomId, @RequestBody @Valid ChatRoomForm chatRoomForm) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatRoomService.inviteChatRoomMember(chatRoomId, currentMemberId, chatRoomForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 채팅방에서 강퇴(그룹) / 방장권한
    @DeleteMapping("/group/{chatRoomId}")
    public ApiResponse<String> kickOutChatRoomMember(@PathVariable Long chatRoomId, @RequestBody @Valid ChatRoomForm chatRoomForm) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatRoomService.kickOutChatRoomMember(chatRoomId, currentMemberId, chatRoomForm);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }

    // 채팅방 나가기(DM/그룹)
    @DeleteMapping("/{chatRoomId}")
    public ApiResponse<String> leaveChatRoomMember(@PathVariable Long chatRoomId) {
        Long currentMemberId = AuthUtil.getCurrentMemberId();
        chatRoomService.leaveChatRoomMember(chatRoomId, currentMemberId);
        return ApiResponse.of(ReturnCode.SUCCESS);
    }
}
