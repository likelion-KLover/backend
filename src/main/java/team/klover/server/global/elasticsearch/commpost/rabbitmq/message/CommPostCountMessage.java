package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.enums.Country;

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
