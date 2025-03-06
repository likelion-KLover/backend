package team.klover.server.global.fcm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.klover.server.domain.notification.entity.NotificationMessage;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.global.translation.service.TranslationService;

@Service
@RequiredArgsConstructor
public class MessageBuildService {

    private final TranslationService translationService;
    private final String titleTemplate = "You have a new notification!";

    public NotificationMessage buildCommPostLikeMessage(NotificationMessage message, String userLanguage) throws JsonProcessingException {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        String bodyTemplate = "{receiverNickname}'s post {content} was liked by {actorNickname}.";
        String title = translationService.translateText(titleTemplate, userLanguage);
        String body = translationService.translateTextPreservingVariables(bodyTemplate, userLanguage);

        body = body.replace("{receiverNickname}", receiverNickname)
                .replace("{actorNickname}", actorNickname)
                .replace("{content}", content);
        return message.toBuilder().title(title).body(body).build();
    }

    public NotificationMessage buildCommentCreateMessage(NotificationMessage message, String userLanguage) throws JsonProcessingException {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        String title = translationService.translateText(titleTemplate, userLanguage);
        String bodyTemplate = "{receiverNickname}'s post {content} was commented on by {actorNickname}.";
        String body = translationService.translateTextPreservingVariables(bodyTemplate, userLanguage);

        body = body.replace("{receiverNickname}", receiverNickname)
                .replace("{actorNickname}", actorNickname)
                .replace("{content}", content);

        return message.toBuilder().title(title).body(body).build();
    }

    public NotificationMessage buildCommentLikeMessage(NotificationMessage message, String userLanguage) throws JsonProcessingException {
        String receiverNickname = (String) message.getCustomField().get(CustomFieldKey.RECEIVER_NICKNAME.getKeyName());
        String actorNickname = (String) message.getCustomField().get(CustomFieldKey.ACTOR_NICKNAME.getKeyName());
        String content = (String) message.getCustomField().get(CustomFieldKey.CONTENT.getKeyName());

        String title = translationService.translateText(titleTemplate, userLanguage);
        String bodyTemplate = "{receiverNickname}'s comment {content} was liked by {actorNickname}.";
        String body = translationService.translateTextPreservingVariables(bodyTemplate, userLanguage);

        body = body.replace("{receiverNickname}", receiverNickname)
                .replace("{actorNickname}", actorNickname)
                .replace("{content}", content);

        return message.toBuilder().title(title).body(body).build();
    }
}