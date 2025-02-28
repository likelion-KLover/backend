package team.klover.server.global.rabbitMQ.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.commPost.event.CommPostLikedEvent;
import team.klover.server.domain.community.comment.entity.Comment;
import team.klover.server.domain.community.comment.event.CommentCreatedEvent;
import team.klover.server.domain.community.comment.event.CommentLikedEvent;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.domain.notification.enums.EventType;
import team.klover.server.domain.notification.enums.TargetObject;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

import static team.klover.server.global.util.Util.truncateContent;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Comment 생성 알림
     */
    public void sendNotification(CommentCreatedEvent event) {
        CommPost commPost = event.getCommPost();
        Comment comment = event.getComment();
        String shortenedContent = truncateContent(commPost.getContent(), 10);

        NotificationMessage message = NotificationMessage.builder()
                .object(TargetObject.COMMPOST)
                .eventType(EventType.COMMENT_CREATE)
                .objectId(commPost.getId())
                .build();

        message.addCustomField(CustomFieldKey.CONTENT, shortenedContent);
        message.addCustomField(CustomFieldKey.RECEIVER_KEY, commPost.getMember().getNickname());
        message.addCustomField(CustomFieldKey.ACTOR_KEY, comment.getMember().getNickname());

        rabbitTemplate.convertAndSend(QueueNames.COMMENT_NOTIFICATION.name(), message);
        System.out.println("Sent message: " + message);
    }

    /**
     * Comment 좋아요 알림
     */
    public void sendNotification(CommentLikedEvent event) {
        Comment comment = event.getComment();
        Member member = event.getMember(); // 좋아요를 누른 member
        String shortenedContent = truncateContent(comment.getContent(), 10);

        NotificationMessage message = NotificationMessage.builder()
                .object(TargetObject.COMMENT)
                .eventType(EventType.COMMENT_LIKE)
                .objectId(comment.getId())
                .build();

        message.addCustomField(CustomFieldKey.CONTENT, shortenedContent);
        message.addCustomField(CustomFieldKey.RECEIVER_KEY, comment.getMember().getNickname());
        message.addCustomField(CustomFieldKey.ACTOR_KEY, member.getNickname());

        rabbitTemplate.convertAndSend(QueueNames.COMMENT_NOTIFICATION.name(), message);
        System.out.println("Sent message: " + message);

    }

    /**
     * CommPost 좋아요 알림
     */
    public void sendNotification(CommPostLikedEvent event) {
        CommPost commPost = event.getCommPost();
        Member member = event.getMember();
        String shortenedContent = truncateContent(commPost.getContent(), 10);

        NotificationMessage message = NotificationMessage.builder()
                .object(TargetObject.COMMPOST)
                .eventType(EventType.COMMPOST_LIKE)
                .objectId(commPost.getId())
                .build();

        message.addCustomField(CustomFieldKey.CONTENT, shortenedContent);
        message.addCustomField(CustomFieldKey.RECEIVER_KEY, commPost.getMember().getNickname());
        message.addCustomField(CustomFieldKey.ACTOR_KEY, member.getNickname());

        rabbitTemplate.convertAndSend(QueueNames.COMMENT_NOTIFICATION.name(), message);
        System.out.println("Sent message: " + message);
    }
}
