package team.klover.server.global.elasticsearch.tourpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

@Getter
@AllArgsConstructor
@Builder
public class ReviewCountMessage {
    private final Long content_id;
    private final Long review_count;

    public ReviewCountMessage(TourPost tourPost, long review_count){
        content_id = tourPost.getContentId();
        this.review_count = review_count;
    }

}
