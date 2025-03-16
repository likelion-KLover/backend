package team.klover.server.global.elasticsearch.member.springevent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.global.elasticsearch.member.springevent.event.MemberDeleteEvent;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MemberDeletionMessage {
    private Long id;

    public MemberDeletionMessage(MemberDeleteEvent event){
        this.id = event.getMember().getId();
    }
}
