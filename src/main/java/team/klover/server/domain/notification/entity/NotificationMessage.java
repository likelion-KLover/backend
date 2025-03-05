package team.klover.server.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.domain.notification.enums.CustomFieldKey;
import team.klover.server.domain.notification.enums.EventType;
import team.klover.server.domain.notification.enums.TargetObject;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NotificationMessage {
    private String title; // push 알림의 제목
    private String body; // push 알림의 내용
    private TargetObject object ;  // 알림 클릭 시 이동할 엔티티 (ex : commPost)
    private EventType eventType;  // 이벤트 타입 (ex: "COMMENT_LIKE", "COMMPOST_LIKE")
    private Long objectId;     // 댓글 ID or 게시글 ID (object + targetId를 이용해 프론트에서 redirect 가능하게)
    @Builder.Default
    private Map<String, Object> customField = new HashMap<>(); // 추가 알림정보에 쓸 데이터

    public void addCustomField(CustomFieldKey key, Object value) {
        this.customField.put(key.getKeyName(), value);
    }
}