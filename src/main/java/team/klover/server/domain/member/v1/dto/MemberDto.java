package team.klover.server.domain.member.v1.dto;

import lombok.*;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.Country;
import team.klover.server.global.elasticsearch.member.doc.MemberDoc;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String email;
    private String nickname;
    private String profileUrl;
    private String role;
    private Long id;
    private Country country;
    private String provider;

    public MemberDto(Member member) {
        id = member.getId();
        nickname = member.getNickname();
        profileUrl = member.getProfileUrl();
        role = member.getRole().name();
        email = member.getEmail();
        country = member.getCountry();
        provider = member.getSocialProvider().name();
    }

    public MemberDto(MemberDoc memberDoc){
        id = memberDoc.getId();
        nickname = memberDoc.getNickname();
        profileUrl = memberDoc.getProfile_url();
        role = memberDoc.getRole().name();
        email = memberDoc.getEmail();
        country = memberDoc.getCountry();
        provider = memberDoc.getSocial_provider().name();
    }
}