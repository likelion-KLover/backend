package team.klover.server.global.elasticsearch.commpost.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;

import java.util.List;

@Getter
public class NicknameUpdateEvent extends ApplicationEvent {
    private final CommPost commPost;
    private final String nickname;

    public NicknameUpdateEvent(Object src, CommPost commPost, String nickname){
        super(src);
        this.commPost = commPost;
        this.nickname = nickname;
    }
}
