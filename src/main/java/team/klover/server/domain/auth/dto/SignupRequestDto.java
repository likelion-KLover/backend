package team.klover.server.domain.auth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    private String email;
    private String password;
    private String nickname;
}
