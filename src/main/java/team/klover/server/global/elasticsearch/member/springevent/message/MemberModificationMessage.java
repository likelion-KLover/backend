package team.klover.server.global.elasticsearch.member.springevent.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.klover.server.global.elasticsearch.member.springevent.event.MemberDeleteEvent;
import team.klover.server.global.elasticsearch.member.springevent.event.MemberUpdateEvent;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MemberModificationMessage {
    private Long id;
    private String email;
    private String social_provider;
    private String nickname;
    private String profile_url;
    private String country;
    private String role;


    public MemberModificationMessage(MemberUpdateEvent event){
        this.id = event.getMember().getId();
        this.country = event.getMember().getCountry().name();
        this.email = event.getMember().getEmail();
        this.profile_url = event.getMember().getProfileUrl();
        this.nickname = event.getMember().getNickname();
        this.social_provider = event.getMember().getSocialProvider().name();
        this.role = event.getMember().getRole().name();
    }
}
