package team.klover.server.domain.notification.eventListener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.klover.server.domain.community.commPost.event.CommPostLikedEvent;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.domain.notification.enums.EventType;
import team.klover.server.domain.notification.enums.TargetObject;
import team.klover.server.global.rabbitMQ.producer.RabbitMQProducer;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

import static team.klover.server.global.util.Util.truncateContent;

@Component
@RequiredArgsConstructor
public class CommPostEventListener {

    private final RabbitMQProducer producer;

    @EventListener
    public void handleCommPostLikedEvent(CommPostLikedEvent event) {
        NotificationMessage message = createCommPostLikedMessage(event);
        producer.sendNotification(QueueNames.COMMPOST_NOTIFICATION.name(), message);
    }

    private NotificationMessage createCommPostLikedMessage(CommPostLikedEvent event) {
        String shortenedContent = truncateContent(event.getCommPost().getContent(), 10);

        NotificationMessage message = NotificationMessage.builder()
                .object(TargetObject.COMMPOST)
                .eventType(EventType.COMMPOST_LIKE)
                .objectId(event.getCommPost().getId())
                .build();

        message.addCustomField(CustomFieldKey.CONTENT, shortenedContent);
        message.addCustomField(CustomFieldKey.RECEIVER_NICKNAME, event.getCommPost().getMember().getNickname());
        message.addCustomField(CustomFieldKey.RECEIVER_ID, event.getCommPost().getMember().getId());
        message.addCustomField(CustomFieldKey.ACTOR_NICKNAME, event.getMember().getNickname());
        message.addCustomField(CustomFieldKey.RECEIVER_COUNTRY, event.getCommPost().getMember().getCountry().name());
        return message;
    }
}