package team.klover.server.global.elasticsearch.commpost.springevent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.community.commPost.entity.CommPost;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommPostModificationMessage {
    private Long id;
    private String language;
    private List<String> image_urls;
    private String content;
    private Double mapx;
    private Double mapy;
    private Long member_id;
    private LocalDateTime create_date;
    private LocalDateTime modify_date;

    public CommPostModificationMessage(CommPost commPost){
        id = commPost.getId();
        language = commPost.getLanguage().name();
        image_urls = commPost.getImageUrls();
        content = commPost.getContent();
        mapx = commPost.getMapX();
        mapy = commPost.getMapY();
        member_id = commPost.getMember().getId();
        create_date = commPost.getCreateDate();
        modify_date = commPost.getModifyDate();
    }
}
