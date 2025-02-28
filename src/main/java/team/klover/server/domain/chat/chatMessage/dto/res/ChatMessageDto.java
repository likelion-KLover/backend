package team.klover.server.domain.chat.chatMessage.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDto {
    private Long id;
    private Long memberId;
    private String nickname;
    private String content;
    private LocalDateTime createDate;
}
