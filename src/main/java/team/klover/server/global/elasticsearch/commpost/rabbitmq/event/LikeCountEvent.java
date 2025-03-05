package team.klover.server.global.elasticsearch.commpost.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
public class LikeCountEvent  extends ApplicationEvent {
    private final CommPost commPost;
    private final long likeCount;

    public LikeCountEvent(Object src, CommPost commPost, long likeCount){
        super(src);
        this.commPost = commPost;
        this.likeCount = likeCount;
    }
}
