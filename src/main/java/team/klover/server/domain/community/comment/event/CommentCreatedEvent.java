package team.klover.server.domain.community.comment.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.community.comment.entity.Comment;

@Getter
public class CommentCreatedEvent extends ApplicationEvent {

    private final CommPost commPost;
    private final Comment comment;

    public CommentCreatedEvent(Object source, CommPost commPost, Comment comment) {
        super(source);
        this.commPost = commPost;
        this.comment = comment;
    }
}
