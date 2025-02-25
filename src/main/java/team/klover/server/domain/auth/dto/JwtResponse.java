package team.klover.server.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class JwtResponse {
    private String accessToken;;
    private String refreshToken;
}
