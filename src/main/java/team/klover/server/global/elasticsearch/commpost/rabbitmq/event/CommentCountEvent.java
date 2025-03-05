package team.klover.server.global.elasticsearch.commpost.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
public class CommentCountEvent extends ApplicationEvent {
    private final CommPost commPost;
    private final long commentCount;

    public CommentCountEvent(Object src, CommPost commPost, long commentCount){
        super(src);
        this.commPost = commPost;
        this.commentCount =commentCount;
    }
}
