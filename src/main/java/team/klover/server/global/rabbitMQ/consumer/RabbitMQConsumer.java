package team.klover.server.global.rabbitMQ.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
//import team.klover.server.global.fcm.service.FCMService;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {

    //private final FCMService fcmService;
    private final ObjectMapper objectMapper;
    @RabbitListener(queues = "COMMENT_NOTIFICATION")
    public void consumeCommentMessage(String message) throws JsonProcessingException {
        NotificationMessage converted = objectMapper.readValue(message, NotificationMessage.class);
        Long receiverID = safeMemberIdConverter(converted.getCustomField().get(CustomFieldKey.RECEIVER_ID.getKeyName()));
        //fcmService.sendPushNotification(receiverID, converted);
    }

    @RabbitListener(queues = "COMMPOST_NOTIFICATION")
    public void consumeCommPostMessage(String message) throws JsonProcessingException {
        NotificationMessage converted = objectMapper.readValue(message, NotificationMessage.class);
        Long receiverID = safeMemberIdConverter(converted.getCustomField().get(CustomFieldKey.RECEIVER_ID.getKeyName()));
       // fcmService.sendPushNotification(receiverID, converted);
    }

    private Long safeMemberIdConverter(Object memberId) {
        Integer safety = (Integer) memberId;
        return Long.valueOf(safety);
    }
}