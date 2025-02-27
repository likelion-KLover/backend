package team.klover.server.domain.chat.chatRoom.serviceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.chat.chatRoom.dto.req.ChatRoomForm;
import team.klover.server.domain.chat.chatRoom.dto.res.ChatRoomDto;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomMember;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomPage;
import team.klover.server.domain.chat.chatRoom.repository.ChatRoomRepository;
import team.klover.server.domain.chat.chatRoom.service.ChatRoomService;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberV1Repository memberV1Repository;

    // 채팅방 목록 조회(DM/그룹)
    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomDto> findByMemberId(Long currentMemberId, Pageable pageable){
        checkPageSize(pageable.getPageSize());
        Page<ChatRoom> chatRooms = chatRoomRepository.findByMemberId(currentMemberId, pageable);
        return chatRooms.map(this::convertToChatRoomDto);
    }

    // 채팅방 생성(DM/그룹)
    @Override
    @Transactional
    public void addChatRoom(Long currentMemberId, @Valid ChatRoomForm chatRoomForm){
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = memberV1Repository.findById(currentMemberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 새로운 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .title(chatRoomForm.getTitle())
                .member(member) // 방장 지정
                .build();
        // 본인을 맨 앞에 추가
        List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
        chatRoomMembers.add(ChatRoomMember.builder()
                .member(member)
                .chatRoom(chatRoom)
                .build());

        // 추가하려던 멤버 추가 (중복 방지)
        for (ChatRoomMember chatRoomMember : chatRoomForm.getChatRoomMembers()) {
            if (!chatRoomMember.getMember().getId().equals(currentMemberId)) {
                chatRoomMembers.add(ChatRoomMember.builder()
                        .member(chatRoomMember.getMember())
                        .chatRoom(chatRoom)
                        .build());
            }
        }
        chatRoom.setChatRoomMembers(chatRoomMembers);
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방 이름 수정(그룹)
    @Override
    @Transactional
    public void updateChatRoomTitle(Long chatRoomId, Long currentMemberId, @Valid ChatRoomForm chatRoomForm){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자 검증 - 현재 로그인한 사용자의 ID를 가져와서 검증
        Member member = memberV1Repository.findById(currentMemberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        if (!chatRoom.getMember().getId().equals(member.getId())) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        chatRoom.setTitle(chatRoomForm.getTitle());
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방에 초대(그룹)
    @Override
    @Transactional
    public void inviteChatRoomMember(Long chatRoomId, Long currentMemberId, @Valid ChatRoomForm chatRoomForm){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        // 기존 멤버인지 확인
        boolean memberExists = chatRoom.getChatRoomMembers().stream()
                .anyMatch(joinedMember -> joinedMember.getMember().getId().equals(currentMemberId));
        if (!memberExists) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }

        // 초대할 멤버 추가(기존 멤버 아닌 경우에만 추가)
        List<ChatRoomMember> newMembers = chatRoomForm.getChatRoomMembers().stream()
                .filter(newMember -> chatRoom.getChatRoomMembers().stream()
                        .noneMatch(existingMember -> existingMember.getMember().getId().equals(newMember.getMember().getId())))
                .collect(Collectors.toList());
        chatRoom.getChatRoomMembers().addAll(newMembers);
    }

    // 채팅방에서 강퇴(그룹) / 방장권한
    @Override
    @Transactional
    public void kickOutChatRoomMember(Long chatRoomId, Long currentMemberId, @Valid ChatRoomForm chatRoomForm){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 방장인지 확인 (방장만 강퇴 가능)
        if (!chatRoom.getMember().getId().equals(currentMemberId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }

        // 강퇴할 멤버 목록 가져오기 & 채팅방 참여 목록에서 제거
        List<Long> kickMemberIds = chatRoomForm.getChatRoomMembers().stream()
                .map(member -> member.getMember().getId())
                .collect(Collectors.toList());
        chatRoom.getChatRoomMembers().removeIf(existingMember ->
                kickMemberIds.contains(existingMember.getMember().getId()));
    }

    // 채팅방 나가기(DM/그룹)
    @Override
    @Transactional
    public void leaveChatRoomMember(Long chatRoomId, Long currentMemberId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        Member member = memberV1Repository.findById(currentMemberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 방장인지 확인
        if (chatRoom.getMember().getId().equals(currentMemberId)) {
            // 방장이 나갈 경우, 다음 멤버를 방장으로 지정
            chatRoom.getChatRoomMembers().remove(member);
            if (!chatRoom.getChatRoomMembers().isEmpty()) {
                ChatRoomMember nextOwner = chatRoom.getChatRoomMembers().get(0);
                chatRoom.setMember(nextOwner.getMember());
            }
        } else {
            // 방장이 아니라면 그냥 채팅방에서 나가기
            chatRoom.getChatRoomMembers().remove(member);
        }
    }

    // 요청 페이지 수 제한
    private void checkPageSize(int pageSize) {
        int maxPageSize = ChatRoomPage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // ChatRoom을 ChatRoomDto로 변환
    private ChatRoomDto convertToChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .memberId(chatRoom.getMember().getId())
                .title(chatRoom.getTitle())
                .chatRoomMembers(chatRoom.getChatRoomMembers())
                .build();
    }
}
