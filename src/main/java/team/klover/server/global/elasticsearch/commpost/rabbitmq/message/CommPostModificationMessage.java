package team.klover.server.global.elasticsearch.commpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.community.commPost.entity.CommPost;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CommPostModificationMessage {
    List<String> image_urls;
    String content;
    Double mapX;
    Double mapY;

    public CommPostModificationMessage(CommPost commPost){
        image_urls = commPost.getImageUrls();
        content = commPost.getContent();
        mapX = commPost.getMapX();
        mapY = commPost.getMapY();
    }
}
