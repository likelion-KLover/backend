package team.klover.server.domain.chat.chatRoom.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.klover.server.domain.chat.chatRoom.entity.ChatRoomMember;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomForm {
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
}
