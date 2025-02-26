package team.klover.server.domain.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import team.klover.server.domain.member.v1.dto.MemberDto;
import team.klover.server.domain.member.v1.entity.Member;
import team.klover.server.domain.member.v1.enums.MemberRole;

import java.util.Locale;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponse {
    MemberDto memberDto;

    private String accessToken;
    private String refreshToken;
}
