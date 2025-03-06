package team.klover.server.global.fcm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.global.i18n.service.LocaleMessageService;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageBuildService {

    public NotificationMessage buildCommPostLikeMessage(NotificationMessage message, String userLanguage) {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        Locale locale = new Locale(userLanguage);
        String title = getTitleByLocale(locale);
        String body = LocaleMessageService.getMessage("notification.commPostLike", new Object[]{receiverNickname, content, actorNickname}, locale);

        return message.toBuilder().title(title).body(body).build();
    }

    public NotificationMessage buildCommentCreateMessage(NotificationMessage message, String userLanguage) {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        Locale locale = new Locale(userLanguage);
        String title = getTitleByLocale(locale);
        String body = LocaleMessageService.getMessage("notification.commentCreate", new Object[]{receiverNickname, content, actorNickname}, locale);

        return message.toBuilder().title(title).body(body).build();
    }

    public NotificationMessage buildCommentLikeMessage(NotificationMessage message, String userLanguage) {
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