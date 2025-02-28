package team.klover.server.domain.notification.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.domain.community.comment.event.CommentCreatedEvent;
import team.klover.server.domain.community.comment.event.CommentLikedEvent;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;

@Component
@RequiredArgsConstructor
public class CommentEventListener {

    private final RabbitMQProducer producer;

    @EventListener
    public void handleCommentCreatedEvent(CommentCreatedEvent event) {
        producer.sendNotification(event);
    }

    @EventListener
    public void handleCommentLikedEvent(CommentLikedEvent event) {
        producer.sendNotification(event);
    }
}
