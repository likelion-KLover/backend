package team.klover.server.global.rabbitMQ.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.global.elasticsearch.commpost.rabbitmq.event.*;
import team.klover.server.global.elasticsearch.commpost.rabbitmq.message.*;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.event.TourPostCountEvent;
import team.klover.server.global.elasticsearch.tourpost.rabbitmq.message.TourPostCountMessage;
import team.klover.server.global.rabbitMQ.queueNames.QueueNames;

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
    public void notifyCommPostCount(CommPostCountEvent event){
        CommPostCountMessage message = new CommPostCountMessage(event.getCommPost());
        String json = objectMapper.writeValueAsString(message);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_COUNT.name(), json);
    }

    /**
     *  엘라스틱서치 리뷰 개수 이벤트 (TourPost)
     */
    @SneakyThrows
    public void notifyTourPostCount(TourPostCountEvent event){
        TourPostCountMessage message = new TourPostCountMessage(event.getTourPost());
        String json = objectMapper.writeValueAsString(message);

        rabbitTemplate.convertAndSend(QueueNames.ES_TOURPOST_UPDATE.name(), json);
    }

    /**
     * 엘라스틱서치 유저 닉네임 변경 이벤트 (CommPost)
     */
    @SneakyThrows
    public void notifyNicknameModification(NicknameUpdateEvent event){
        NicknameModificationMessage message = new NicknameModificationMessage(event.getMember());
        String json = objectMapper.writeValueAsString(message);


        rabbitTemplate.convertAndSend(QueueNames.ES_MEMBER_UPDATE.name(),json);
    }

    /**
     * 엘라스틱서치 커뮤니티 게시글 업데이트 이벤트 (CommPost)
     */
    @SneakyThrows
    public void notifyCommPostModification(CommPostUpdateEvent event){
        CommPostModificationMessage message = new CommPostModificationMessage(event.getCommPost());
        String json = objectMapper.writeValueAsString(message);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_UPDATE.name(), json);
    }

    /**
     * 엘라스틱서치 커뮤니티 게시글 삭제 이벤트 (CommPost)
     */
    @SneakyThrows
    public void notifyCommPostDeletion(CommPostDeleteEvent event){
        CommPostDeletionMessage message = new CommPostDeletionMessage(event.getCommPost());
        String json = objectMapper.writeValueAsString(message);

        rabbitTemplate.convertAndSend(QueueNames.ES_COMMPOST_DELETE.name(), json);
    }
}
