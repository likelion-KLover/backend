package team.klover.server.domain.chat.chatRoom.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.chat.chatRoom.dto.req.ChatRoomForm;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomPage;
import team.klover.server.domain.chat.chatRoom.repository.ChatRoomRepository;
import team.klover.server.domain.chat.chatRoom.service.ChatRoomService;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberV1Repository memberV1Repository;

    // 채팅방 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoom> findByMemberId(Long memberId, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        return chatRoomRepository.findByMemberId(memberId, pageable);
    }

    // 채팅방 생성
    @Override
    @Transactional
    public void addChatRoom(Long memberId, ChatRoomForm chatRoomForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = memberV1Repository.findById(memberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .chatRoomMembers(chatRoomForm.getChatRoomMembers())
                .build();
        chatRoomRepository.save(chatRoom);
    }

    // 요청 페이지 수 제한
    private void checkPageSize(int pageSize) {
        int maxPageSize = ChatRoomPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }
}
