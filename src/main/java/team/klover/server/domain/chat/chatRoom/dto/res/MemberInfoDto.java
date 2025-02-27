package team.klover.server.domain.chat.chatRoom.dto.res;

import lombok.*;

@Data
@Builder
public class MemberInfoDto {
    private Long memberId;
    private String memberNickname;
}
