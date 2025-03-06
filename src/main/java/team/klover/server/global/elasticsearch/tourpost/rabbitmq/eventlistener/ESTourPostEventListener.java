package team.klover.server.global.elasticsearch.tourpost.rabbitmq.eventlistener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.event.TourPostCountEvent;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;

@Component
@RequiredArgsConstructor
public class ESTourPostEventListener {
    private final RabbitMQProducer producer;

    @EventListener
    public void handleReviewCount(TourPostCountEvent event){
        producer.notifyTourPostCount(event);
    }

}
