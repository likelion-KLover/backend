package team.klover.server.domain.community.commPost.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DetailCommPostDto {
    private Long memberId;
    private String nickname;
    private int likeCount;
    private Double mapX;
    private Double mapY;
    private String content;
    private String imageUrl;
    private LocalDateTime createDate;
}
