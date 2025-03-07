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
public class CommPostCountMessage {
    private Long id;
    private String language;

    public CommPostCountMessage(CommPost commPost){
        id = commPost.getId();
        language = commPost.getLanguage().name();
    }
}
