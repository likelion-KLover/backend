package team.klover.server.domain.community.commPost.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommPostDto {
    private Long testMemberId;
    private String testMemberNickname;
    private Float mapX;
    private Float mapY;
    private String imageUrl;
}
