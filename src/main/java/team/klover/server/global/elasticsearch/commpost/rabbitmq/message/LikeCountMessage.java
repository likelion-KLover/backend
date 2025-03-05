package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
@AllArgsConstructor
@Builder
public class LikeCountMessage {
    private final Long id;
    private final Long like_count;

    public LikeCountMessage(CommPost commPost, Long like_count){
        id = commPost.getId();
        this.like_count = like_count;
    }
}
