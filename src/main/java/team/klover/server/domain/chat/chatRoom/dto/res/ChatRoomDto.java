package team.klover.server.domain.chat.chatRoom.dto.res;

import lombok.Builder;
import lombok.Data;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomMember;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ChatRoomDto {
    private Long memberId;
    private String title;
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
}
