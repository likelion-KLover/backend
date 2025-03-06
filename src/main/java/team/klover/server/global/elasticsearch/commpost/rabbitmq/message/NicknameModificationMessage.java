package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.Country;

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
