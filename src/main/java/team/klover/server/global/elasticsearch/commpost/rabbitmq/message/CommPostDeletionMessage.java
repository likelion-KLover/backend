package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.enums.Country;

@Getter
@AllArgsConstructor
@Builder
public class CommPostDeletionMessage {
    private final Long id;
    private final Country language;

    public CommPostDeletionMessage(CommPost commPost){
        this.id = commPost.getId();
        this.language = commPost.getLanguage();
    }
}
