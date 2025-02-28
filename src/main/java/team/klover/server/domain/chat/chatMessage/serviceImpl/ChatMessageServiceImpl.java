package team.klover.server.domain.chat.chatMessage.serviceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.klover.server.domain.chat.chatMessage.dto.req.ChatMessageForm;
import team.klover.server.domain.chat.chatMessage.dto.res.ChatMessageDto;
import team.klover.server.domain.chat.chatMessage.entity.ChatMessage;
import team.klover.server.domain.chat.chatMessage.entity.ChatMessagePage;
import team.klover.server.domain.chat.chatMessage.entity.MessageContent;
import team.klover.server.domain.chat.chatMessage.repository.ChatMessageRepository;
import team.klover.server.domain.chat.chatMessage.repository.MessageContentRepository;
import team.klover.server.domain.chat.chatMessage.service.ChatMessageService;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoom;
import team.klover.server.domain.chat.chatRoom.repository.ChatRoomRepository;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.repository.MemberV1Repository;
import team.klover.server.global.exception.KloverRequestException;
import team.klover.server.global.exception.ReturnCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberV1Repository memberV1Repository;
    private final RabbitTemplate rabbitTemplate;
    private final MessageContentRepository messageContentRepository;

    // 해당 채팅방의 메시지 조회
    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageDto> findByChatRoomId(Long currentMemberId, Long chatRoomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        checkPageSize(pageable.getPageSize());

        // 채팅방 참여멤버만 메시지 조회 가능
        boolean memberExists = chatRoom.getChatRoomMembers().stream()
                .anyMatch(joinedMember -> joinedMember.getMember().getId().equals(currentMemberId));
        if (!memberExists) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        Page<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId, pageable);

        List<String> stringMessageIds = chatMessages.stream()
                .map(chatMessage -> String.valueOf(chatMessage.getId()))
                .collect(Collectors.toList());
        Map<Long, String> messageContentMap = messageContentRepository.findByIdIn(stringMessageIds).stream()
                .collect(Collectors.toMap(message -> Long.parseLong(message.getId()), MessageContent::getContent));

        return chatMessages.map(chatMessage -> {
            String content = messageContentMap.getOrDefault(chatMessage.getId(), ""); // 없으면 빈 문자열
            return convertToChatMessageDto(chatMessage, content);
        });
    }

    // 해당 채팅방에서 메시지 검색(닉네임/내용)
    @Override
    @Transactional
    public Page<ChatMessageDto> searchByKeyword(Long currentMemberId, Long chatRoomId, String keyword, Pageable pageable){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        checkPageSize(pageable.getPageSize());

        // 채팅방 참여멤버만 메시지 조회 가능
        boolean memberExists = chatRoom.getChatRoomMembers().stream()
                .anyMatch(joinedMember -> joinedMember.getMember().getId().equals(currentMemberId));
        if (!memberExists) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        // 1️⃣ 채팅방 내 모든 메시지 ID 조회
        Page<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId, pageable);
        List<String> stringMessageIds = chatMessages.stream()
                .map(chatMessage -> String.valueOf(chatMessage.getId()))
                .collect(Collectors.toList());

        // 2️⃣ MongoDB에서 해당 ID들의 메시지 내용 조회
        Map<Long, String> messageContentMap = messageContentRepository.findByIdIn(stringMessageIds).stream()
                .collect(Collectors.toMap(message -> Long.parseLong(message.getId()), MessageContent::getContent));

        // 3️⃣ 키워드 포함 여부 검사 후 필터링
        List<ChatMessage> filteredMessages = chatMessages.stream()
                .filter(chatMessage -> {
                    String content = messageContentMap.getOrDefault(chatMessage.getId(), "");
                    return content.contains(keyword); // 키워드 포함 여부 확인
                })
                .collect(Collectors.toList());

        // 4️⃣ 필터링된 메시지를 DTO로 변환
        List<ChatMessageDto> chatMessageDtos = filteredMessages.stream()
                .map(chatMessage -> {
                    String content = messageContentMap.getOrDefault(chatMessage.getId(), "");
                    return convertToChatMessageDto(chatMessage, content);
                })
                .collect(Collectors.toList());

        // 5️⃣ Page 객체로 변환 후 반환
        return new PageImpl<>(chatMessageDtos, pageable, chatMessageDtos.size());
    }

    // 해당 채팅방에서 메시지 생성
    @Override
    @Transactional
    public void writeChatMessage(Long currentMemberId, Long chatRoomId, @Valid ChatMessageForm chatMessageForm) {
        // 현재 로그인한 사용자의 member 객체를 가져오는 메서드
        Member member = memberV1Repository.findById(currentMemberId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 채팅방 참여멤버만 메시지 생성 가능
        boolean memberExists = chatRoom.getChatRoomMembers().stream()
                .anyMatch(joinedMember -> joinedMember.getMember().getId().equals(currentMemberId));
        if (!memberExists) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        ChatMessage chatMessage = ChatMessage.builder()
                .member(member)
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(chatMessage);

        // MongoDB에 메시지 본문 저장
        MessageContent messageContent = MessageContent.builder()
                .id(String.valueOf(chatMessage.getId())) // ChatMessage의 ID를 키로 사용
                .content(chatMessageForm.getContent())
                .build();
        messageContentRepository.save(messageContent);
        rabbitTemplate.convertAndSend("amq.topic", "chatRoomId: " + chatRoomId + "MessageCreated: ", chatMessage);
    }

    // 해당 메시지 삭제
    @Override
    @Transactional
    public void deleteChatMessage(Long currentMemberId, Long messageId) {
        ChatMessage chatMessage = chatMessageRepository.findById(messageId).orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));

        // 작성자만 삭제 가능
        if (!chatMessage.getMember().getId().equals(currentMemberId)) {
            throw new KloverRequestException(ReturnCode.NOT_AUTHORIZED);
        }
        messageContentRepository.deleteById(String.valueOf(messageId));
        chatMessageRepository.delete(chatMessage);
    }

    // 해당 채팅방의 모든 메시지 삭제
    @Override
    @Transactional
    public void deleteAllChatMessages(Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new KloverRequestException(ReturnCode.NOT_FOUND_ENTITY));
        List<ChatMessage> messages = chatMessageRepository.findByChatRoom(chatRoom);
        chatMessageRepository.deleteAll(messages);
        messages.forEach(message -> messageContentRepository.deleteById(String.valueOf(message.getId())));
    }

    // 요청 메시지 수 제한
    private void checkPageSize(int pageSize) {
        int maxPageSize = ChatMessagePage.getMaxPageSize();
        if (pageSize > maxPageSize) {
            throw new KloverRequestException(ReturnCode.WRONG_PARAMETER);
        }
    }

    // ChatMessage를 ChatMessageDto로 변환
    private ChatMessageDto convertToChatMessageDto(ChatMessage chatMessage, String content) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .memberId(chatMessage.getMember().getId())
                .nickname(chatMessage.getMember().getNickname())
                .content(content)
                .createDate(chatMessage.getCreateDate())
                .build();
    }
}
