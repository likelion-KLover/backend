package team.klover.server.domain.member.v1.dto;

import lombok.*;
import team.klover.server.domain.member.v1.enums.Country;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class MemberUpdateParam {
    private String nickname; //new nickname
    private Country country;

}
