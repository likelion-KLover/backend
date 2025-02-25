package team.klover.server.domain.member.v1.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class MemberUpdateParam {
    private String nickname; //new nickname
    private String profileUrl; //oldProfileUrl
    private String country;

    public void changeProfileUrl(String newUrl) {
        this.profileUrl = newUrl;
    }

}
