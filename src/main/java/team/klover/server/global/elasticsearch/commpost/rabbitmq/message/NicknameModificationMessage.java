package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
@AllArgsConstructor
@Builder
public class NicknameModificationMessage {
    private final Long id;
    private final String nickname;

    public NicknameModificationMessage(CommPost commPost, String nickname){
        id = commPost.getId();
        this.nickname = nickname;
    }
}
