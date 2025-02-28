package team.klover.server.domain.notification.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.domain.community.commPost.event.CommPostLikedEvent;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;

@Component
@RequiredArgsConstructor
public class CommPostEventListener {

    private final RabbitMQProducer producer;

    @EventListener
    public void handleCommPostLikedEvent(CommPostLikedEvent event) {
        producer.sendNotification(event);
    }
}
