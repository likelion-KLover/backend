package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.enums.Country;

@Getter
@AllArgsConstructor
@Builder
public class NicknameModificationMessage {
    private final Long id;
    private final Country language;
    private final String nickname;

    public NicknameModificationMessage(CommPost commPost, String nickname){
        id = commPost.getId();
        language = commPost.getLanguage();
        this.nickname = nickname;
    }
}
