package team.klover.server.domain.community.commPost.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommPostDto {
    private Long memberId;
    private String nickname;
    private Double mapX;
    private Double mapY;
    private List<String> imageUrls;
    private LocalDateTime createDate;
}
