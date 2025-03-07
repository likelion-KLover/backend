package team.klover.server.global.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.domain.notification.enums.EventType;
import team.klover.server.global.redis.RedisService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final MessageBuildService messageBuildService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    public void sendPushNotification(Long receiverId, NotificationMessage message) throws JsonProcessingException {

//        String fcmToken = redisService.getFCMToken(receiverId);
        String fcmToken = "tmptmptmptmp";
        // rabbitMQListener Exception 방지용 임시 토큰 프론트 측에서 fcm 토큰 발급 후 백에 넘겨주는 것 구현해야 함
        // rabbitMQ 무한 재시도 (cunsume 재시도) 방지 로직 구현 필요
        EventType eventType = message.getEventType();
        NotificationMessage convertedMessage = null;

        String userLanguage = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_COUNTRY.getKeyName());

        switch (eventType) {
            case COMMENT_CREATE: {
                convertedMessage = messageBuildService.buildCommentCreateMessage(message, userLanguage);
                break;
            }
            case COMMENT_LIKE: {
                convertedMessage = messageBuildService.buildCommentLikeMessage(message, userLanguage);
                break;
            }
            case COMMPOST_LIKE: {
                convertedMessage = messageBuildService.buildCommPostLikeMessage(message, userLanguage);
                break;
            }
        }

        assert convertedMessage != null;
        Notification notification = Notification.builder()
                .setTitle(convertedMessage.getTitle())
                .setBody(convertedMessage.getBody())
                .build();

        String serializedNotification = objectMapper.writeValueAsString(convertedMessage);

        Message fcmMessage = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .putData("targetData", serializedNotification)
                .build();

        Optional.ofNullable(fcmMessage).ifPresent(msg -> {
            try {
                FirebaseMessaging.getInstance().send(msg);
                log.info("FCM 메시지 전송 완료");
            } catch (FirebaseMessagingException e) {
                log.error("FCM 메시지 전송 실패: " + e.getMessage());
            }
        });

        redisService.saveFCMMessage(receiverId, serializedNotification);
    }
}