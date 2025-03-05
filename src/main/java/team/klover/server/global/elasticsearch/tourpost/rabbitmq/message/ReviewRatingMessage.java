package team.klover.server.global.elasticsearch.tourpost.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

@Getter
@AllArgsConstructor
@Builder
public class ReviewRatingMessage {
    private final Long content_id;
    private final double rating_average;

    public ReviewRatingMessage(TourPost tourPost, Double rating_average){
        content_id = tourPost.getContentId();
        this.rating_average = rating_average;
    }


}
