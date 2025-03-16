package team.klover.server.global.elasticsearch.member.springevent.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import team.klover.server.domain.member.v1.entity.Member;

@Getter
public class MemberDeleteEvent extends ApplicationEvent {
    private final Member member;
    public MemberDeleteEvent(Object src, Member member){
        super(src);
        this.member = member;
    }
}
