package team.klover.server.global.fcm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.klover.server.domain.member.v1.service.MemberV1Service;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.global.i18n.service.LocaleMessageService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageBuildService {

    private final MemberV1Service memberV1Service;

    public NotificationMessage buildMessage(NotificationMessage message, Long receiverId) {
        NotificationMessage convertedMessage = null;
        String userLanguage = memberV1Service.getMemberById(receiverId).getCountry().name();

        switch (message.getEventType()) {
            case COMMENT_CREATE: {
                convertedMessage = buildCommentCreateMessage(message, userLanguage);
                break;
            }
            case COMMENT_LIKE: {
                convertedMessage = buildCommentLikeMessage(message, userLanguage);
                break;
            }
            case COMMPOST_LIKE: {
                convertedMessage = buildCommPostLikeMessage(message, userLanguage);
                break;
            }
        }
        return convertedMessage;
    }

    private NotificationMessage buildCommPostLikeMessage(NotificationMessage message, String userLanguage) {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        Locale locale = new Locale(userLanguage);
        String title = getTitleByLocale(locale);
        String body = LocaleMessageService.getMessage("notification.commPostLike", new Object[]{receiverNickname, content, actorNickname}, locale);

        return message.toBuilder().title(title).body(body).build();
    }

    private NotificationMessage buildCommentCreateMessage(NotificationMessage message, String userLanguage) {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        Locale locale = new Locale(userLanguage);
        String title = getTitleByLocale(locale);
        String body = LocaleMessageService.getMessage("notification.commentCreate", new Object[]{receiverNickname, content, actorNickname}, locale);

        return message.toBuilder().title(title).body(body).build();
    }

    private NotificationMessage buildCommentLikeMessage(NotificationMessage message, String userLanguage) {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        Locale locale = new Locale(userLanguage);
        String title = getTitleByLocale(locale);
        String body = LocaleMessageService.getMessage("notification.commentLike", new Object[]{receiverNickname, content, actorNickname}, locale);

        return message.toBuilder().title(title).body(body).build();
    }

    private String getTitleByLocale(Locale locale) {
        return LocaleMessageService.getMessage("notification.title", null, locale);
    }
}