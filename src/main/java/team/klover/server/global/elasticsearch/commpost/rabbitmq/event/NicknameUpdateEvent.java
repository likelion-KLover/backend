package team.klover.server.global.elasticsearch.commpost.rabbitmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.community.commPost.entity.CommPost;
import team.klover.server.domain.member.v1.entity.Member;

import java.util.List;

@Getter
public class NicknameUpdateEvent extends ApplicationEvent {
    private final Member member;
    public NicknameUpdateEvent(Object src, Member member){
        super(src);
        this.member = member;
    }
}
