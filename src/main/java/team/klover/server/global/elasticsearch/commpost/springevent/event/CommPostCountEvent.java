package team.klover.server.global.elasticsearch.commpost.springevent.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
public class CommPostCountEvent extends ApplicationEvent {
    private final CommPost commPost;

    public CommPostCountEvent(Object src, CommPost commPost){
        super(src);
        this.commPost = commPost;
    }
}
