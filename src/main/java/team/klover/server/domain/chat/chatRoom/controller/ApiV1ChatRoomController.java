package team.klover.server.domain.chat.chatRoom.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.klover.server.domain.chat.chatRoom.dto.req.ChatRoomForm;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;
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
    public ApiResponse<ChatRoom> findByMemberId(@ModelAttribute ChatRoomPage request) {
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

    // 채팅방에 초대(그룹)


    // 채팅방에서 강퇴(그룹) / 방장권한


    // 방장권한 위임(그룹) / 방장권한


    // 채팅방 나가기(DM/그룹)

}
