package team.klover.server.domain.community.commPost.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommPostDto {
    private Long memberId;
    private String nickname;
    private Double mapX;
    private Double mapY;
    private String imageUrl;
    private LocalDateTime createDate;
}
