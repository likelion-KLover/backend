package team.klover.server.domain.chat.chatMessage.entity;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class ChatMessagePage {
    // 기본 page, size
    private int page = 0;
    private int size = 20;
    //private LocalDateTime pointTime;
    @Getter
    private static final int maxPageSize = 20;
}
