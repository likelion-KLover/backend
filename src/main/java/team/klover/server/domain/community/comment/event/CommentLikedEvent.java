package team.klover.server.domain.community.comment.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.comment.entity.Comment;
import team.klover.server.domain.member.v1.entity.Member;

@Getter
public class CommentLikedEvent extends ApplicationEvent {

    private final Comment comment;
    private final Member member;

    public CommentLikedEvent(Object source, Comment comment, Member member) {
        super(source);
        this.comment = comment;
        this.member = member;
    }
}
