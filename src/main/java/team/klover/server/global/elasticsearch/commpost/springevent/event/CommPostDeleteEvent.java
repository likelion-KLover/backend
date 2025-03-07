package team.klover.server.global.elasticsearch.commpost.springevent.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;

@Getter
public class CommPostDeleteEvent  extends ApplicationEvent {
    private final CommPost commPost;

    public CommPostDeleteEvent(Object src, CommPost commPost){
        super(src);
        this.commPost = commPost;
    }
}
