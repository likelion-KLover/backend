package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
@AllArgsConstructor
@Builder
public class CommPostDeletionMessage {
    private final Long id;

    public CommPostDeletionMessage(CommPost commPost){
        this.id = commPost.getId();
    }
}
