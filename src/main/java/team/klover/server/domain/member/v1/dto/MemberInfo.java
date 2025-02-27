package team.klover.server.domain.member.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.Country;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberInfo {
    private Long memberId;
    private String email;
    private String nickname;
    private String profileUrl;
    private Country country;

    //타임라인을 작성하기 위해 해당 사용자가 작성한 Comm Post가 나중에 들어갈 예정
    //좋아요 누른 스토리가 여기에 들어갈 수도 있음
    //저장을 누른 Tour Post가 여기에 들어갈 수도 있음

    public MemberInfo(Member member){
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.profileUrl = member.getProfileUrl();
        this.country = member.getCountry();
    }
}
