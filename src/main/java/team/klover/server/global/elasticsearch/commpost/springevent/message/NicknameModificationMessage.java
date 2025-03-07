package team.klover.server.global.elasticsearch.commpost.springevent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.member.v1.entity.Member;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NicknameModificationMessage {
    private Long member_id;
    private String nickname;

    public NicknameModificationMessage(Member member){
        member_id = member.getId();
        this.nickname = member.getNickname();
    }
}
