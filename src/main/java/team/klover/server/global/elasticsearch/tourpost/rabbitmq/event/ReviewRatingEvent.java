package team.klover.server.global.elasticsearch.tourpost.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

@Getter
public class ReviewRatingEvent extends ApplicationEvent {
    private final TourPost tourPost;
    private final double ratingAverage;

    public ReviewRatingEvent(Object src, TourPost tourPost, double ratingAverage){
        super(src);
        this.tourPost = tourPost;
        this.ratingAverage = ratingAverage;
    }

}
