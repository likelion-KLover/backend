package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.enums.Country;

@Getter
@AllArgsConstructor
@Builder
public class CommentCountMessage {
    private final Long id;
    private final Country language;
    private final long comment_count;

    public CommentCountMessage(CommPost commPost, long comment_count){
        id = commPost.getId();
        language = commPost.getLanguage();
        this.comment_count = comment_count;
    }
}
