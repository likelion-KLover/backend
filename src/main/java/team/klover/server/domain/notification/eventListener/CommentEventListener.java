package team.klover.server.domain.notification.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import team.klover.server.domain.community.comment.event.CommentCreatedEvent;
import team.klover.server.domain.community.comment.event.CommentLikedEvent;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.domain.notification.enums.EventType;
import team.klover.server.domain.notification.enums.TargetObject;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

import static team.klover.server.global.util.Util.truncateContent;

@Component
@RequiredArgsConstructor
public class CommentEventListener {

    private final RabbitMQProducer producer;

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentCreatedEvent(CommentCreatedEvent event) {
        NotificationMessage message = createCommentCreatedMessage(event);
        producer.sendNotification(QueueNames.COMMENT_NOTIFICATION.name(), message);
    }

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentLikedEvent(CommentLikedEvent event) {
        NotificationMessage message = createCommentLikedMessage(event);
        producer.sendNotification(QueueNames.COMMENT_NOTIFICATION.name(), message);
    }

    private NotificationMessage createCommentCreatedMessage(CommentCreatedEvent event) {
        String shortenedContent = truncateContent(event.getCommPost().getContent(), 10);

        NotificationMessage message = NotificationMessage.builder()
                .object(TargetObject.COMMPOST)
                .eventType(EventType.COMMENT_CREATE)
                .objectId(event.getCommPost().getId())
                .build();

        message.addCustomField(CustomFieldKey.CONTENT, shortenedContent);
        message.addCustomField(CustomFieldKey.RECEIVER_NICKNAME, event.getCommPost().getMember().getNickname());
        message.addCustomField(CustomFieldKey.RECEIVER_ID, event.getCommPost().getMember().getId());
        message.addCustomField(CustomFieldKey.ACTOR_NICKNAME, event.getComment().getMember().getNickname());
        return message;
    }

    private NotificationMessage createCommentLikedMessage(CommentLikedEvent event) {
        String shortenedContent = truncateContent(event.getComment().getContent(), 10);

        NotificationMessage message = NotificationMessage.builder()
                .object(TargetObject.COMMENT)
                .eventType(EventType.COMMENT_LIKE)
                .objectId(event.getComment().getId())
                .build();

        message.addCustomField(CustomFieldKey.CONTENT, shortenedContent);
        message.addCustomField(CustomFieldKey.RECEIVER_NICKNAME, event.getComment().getMember().getNickname());
        message.addCustomField(CustomFieldKey.RECEIVER_ID, event.getComment().getMember().getId());
        message.addCustomField(CustomFieldKey.ACTOR_NICKNAME, event.getMember().getNickname());
        return message;
    }
}