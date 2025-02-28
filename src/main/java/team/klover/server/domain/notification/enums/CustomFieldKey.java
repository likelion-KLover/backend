package team.klover.server.domain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomFieldKey {
    CONTENT("content"), RECEIVER_KEY("receiver_key"), ACTOR_KEY("actor_key");

    private String keyName;
}
