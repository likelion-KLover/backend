package team.klover.server.global.elasticsearch.tourpost.springevent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TourPostCountMessage {
    private Long content_id;
    private String language;
    private Long common_place_id;

    public TourPostCountMessage(TourPost tourPost){
        content_id = tourPost.getContentId();
        language = tourPost.getLanguage();
        common_place_id = tourPost.getCommonPlaceId();
    }

}
