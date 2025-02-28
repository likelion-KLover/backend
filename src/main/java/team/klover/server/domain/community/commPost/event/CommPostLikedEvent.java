package team.klover.server.domain.community.commPost.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.entity.Member;

@Getter
public class CommPostLikedEvent extends ApplicationEvent {

    private final CommPost commPost;
    private final Member member;

    public CommPostLikedEvent(Object source, CommPost commPost, Member member) {
        super(source);
        this.commPost = commPost;
        this.member = member;
    }
}
