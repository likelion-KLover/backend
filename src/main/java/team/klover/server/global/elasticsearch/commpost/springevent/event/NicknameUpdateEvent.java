package team.klover.server.global.elasticsearch.commpost.springevent.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.member.v1.entity.Member;

@Getter
public class NicknameUpdateEvent extends ApplicationEvent {
    private final Member member;
    public NicknameUpdateEvent(Object src, Member member){
        super(src);
        this.member = member;
    }
}
