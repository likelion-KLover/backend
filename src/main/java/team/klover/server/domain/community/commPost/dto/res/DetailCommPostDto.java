package team.klover.server.domain.community.commPost.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailCommPostDto {
    private Long testMemberId;
    private String testMemberNickname;
    private int likeCount;
    private Float mapX;
    private Float mapY;
    private String content;
    private String imageUrl;
}
