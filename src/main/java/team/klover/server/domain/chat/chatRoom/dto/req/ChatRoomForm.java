package team.klover.server.domain.chat.chatRoom.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 직렬화에서 제외
public class ChatRoomForm {
    private String title;
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
}
