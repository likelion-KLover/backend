package team.klover.server.global.elasticsearch.tourpost.rabbitmq.eventlistener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.global.elasticsearch.commpost.rabbitmq.event.CommPostDeleteEvent;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.event.ReviewCountEvent;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.event.ReviewRatingEvent;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;

@Component
@RequiredArgsConstructor
public class TourPostEventListener {
    private final RabbitMQProducer producer;

    @EventListener
    public void handleReviewCount(ReviewCountEvent event){
        producer.notifyReviewCount(event);
    }

    @EventListener
    public void handleReviewRating(ReviewRatingEvent event){
        producer.notifyReviewAverage(event);
    }
}
