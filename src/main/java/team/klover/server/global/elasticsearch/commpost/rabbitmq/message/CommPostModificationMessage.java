package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.enums.Country;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CommPostModificationMessage {
    Long id;
    Country language;
    List<String> image_urls;
    String content;
    Double mapx;
    Double mapy;

    public CommPostModificationMessage(CommPost commPost){
        id = commPost.getId();
        language = commPost.getLanguage();
        image_urls = commPost.getImageUrls();
        content = commPost.getContent();
        mapx = commPost.getMapX();
        mapy = commPost.getMapY();
    }
}
