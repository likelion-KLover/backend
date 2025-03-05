package team.klover.server.global.elasticsearch.tourpost.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

@Getter
public class ReviewCountEvent extends ApplicationEvent {
    private final TourPost tourPost;
    private final long reviewCount;

    public ReviewCountEvent(Object src, TourPost tourPost, long reviewCount){
        super(src);
        this.tourPost = tourPost;
        this.reviewCount = reviewCount;
    }
}
