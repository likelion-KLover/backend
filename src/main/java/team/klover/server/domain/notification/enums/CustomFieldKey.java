package team.klover.server.domain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomFieldKey {
    CONTENT("content"), RECEIVER_NICKNAME("receiver_nickname"), ACTOR_NICKNAME("actor_nickname"), RECEIVER_ID("receiver_id"), RECEIVER_COUNTRY("receiver_country");

    private String keyName;
}