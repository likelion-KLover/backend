package team.klover.server.global.elasticsearch.member.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import team.klover.server.domain.member.v1.enums.Country;
import team.klover.server.domain.member.v1.enums.MemberRole;
import team.klover.server.domain.member.v1.enums.SocialProvider;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"@timestamp"})
public class MemberDoc {
    private String email;
    private MemberRole role;
    private SocialProvider social_provider;
    private Long id;
    private String nickname;
    private String profile_url;
    private Country country;
}
