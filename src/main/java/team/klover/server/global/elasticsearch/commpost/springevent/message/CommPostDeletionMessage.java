package team.klover.server.global.elasticsearch.commpost.springevent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommPostDeletionMessage {
    private Long id;
    private String language;

    public CommPostDeletionMessage(CommPost commPost){
        this.id = commPost.getId();
        this.language = commPost.getLanguage().name();
    }
}
