package team.klover.server.global.elasticsearch.tourpost.rabbitmq.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.tour.tourPost.entity.TourPost;

@Getter
public class TourPostCountEvent extends ApplicationEvent {
    private final TourPost tourPost;

    public TourPostCountEvent(Object src, TourPost tourPost){
        super(src);
        this.tourPost = tourPost;
    }
}
