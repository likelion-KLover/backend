package team.klover.server.global.rabbitMQ.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
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
import team.klover.server.global.elasticsearch.commpost.rabbitmq.event.*;
import team.klover.server.global.elasticsearch.commpost.rabbitmq.message.*;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.event.ReviewCountEvent;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.event.ReviewRatingEvent;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.message.ReviewCountMessage;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.message.ReviewRatingMessage;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

import static team.klover.server.global.util.Util.truncateContent;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void sendNotification(String queueName, NotificationMessage message) {
        rabbitTemplate.convertAndSend(queueName, objectMapper.writeValueAsString(message));
    }

    /**
     *  엘라스틱서치 댓글 개수 이벤트(CommPost)
     */
    @SneakyThrows
    public void notifyCommentCount(CommentCountEvent event){
        CommentCountMessage message = new CommentCountMessage(event.getCommPost(), event.getCommentCount());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_UPDATE.name(), persistentMessage);
    }

    /**
     *  엘라스틱서치 좋아요 개수 이벤트(CommPost)
     */
    @SneakyThrows
    public void notifyLikeCount(LikeCountEvent event){
        LikeCountMessage message = new LikeCountMessage(event.getCommPost(), event.getLikeCount());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_UPDATE.name(), persistentMessage);
    }


    /**
     *  엘라스틱서치 리뷰 개수 이벤트 (TourPost)
     */
    @SneakyThrows
    public void notifyReviewCount(ReviewCountEvent event){
        ReviewCountMessage message = new ReviewCountMessage(event.getTourPost(), event.getReviewCount());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_TOURPOST_UPDATE.name(), persistentMessage);
    }

    /**
     *  엘라스틱서치 리뷰 평점 이벤트 (TourPost)
     */
    @SneakyThrows
    public void notifyReviewAverage(ReviewRatingEvent event) {
        ReviewRatingMessage message = new ReviewRatingMessage(event.getTourPost(),event.getRatingAverage());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_TOURPOST_UPDATE.name(),persistentMessage);
    }


    /**
     * 엘라스틱서치 유저 닉네임 변경 이벤트 (CommPost)
     */
    @SneakyThrows
    public void notifyNicknameModification(NicknameUpdateEvent event){
        NicknameModificationMessage message = new NicknameModificationMessage(event.getCommPost(), event.getNickname());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_UPDATE.name(),persistentMessage);
    }

    /**
     * 엘라스틱서치 커뮤니티 게시글 업데이트 이벤트 (CommPost)
     */
    @SneakyThrows
    public void notifyCommPostModification(CommPostUpdateEvent event){
        CommPostModificationMessage message = new CommPostModificationMessage(event.getCommPost());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_UPDATE.name(), persistentMessage);
    }

    private Message generateMessage(String json) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);  // 메시지를 Persistent로 설정

        // 메시지 생성
        return new Message(json.getBytes(), messageProperties);
    }

    /**
     * 엘라스틱서치 커뮤니티 게시글 삭제 이벤트 (CommPost)
     */
    @SneakyThrows
    public void notifyCommPostDeletion(CommPostDeleteEvent event){
        CommPostDeletionMessage message = new CommPostDeletionMessage(event.getCommPost());
        String json = objectMapper.writeValueAsString(message);

        Message persistentMessage = generateMessage(json);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_DELETE.name(), persistentMessage);
    }
}
