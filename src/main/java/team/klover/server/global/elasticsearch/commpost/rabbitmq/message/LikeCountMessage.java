package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.enums.Country;

@Getter
@AllArgsConstructor
@Builder
public class LikeCountMessage {
    private final Long id;
    private final Country language;
    private final Long like_count;

    public LikeCountMessage(CommPost commPost, Long like_count){
        id = commPost.getId();
        language = commPost.getLanguage();
        this.like_count = like_count;
    }
}
