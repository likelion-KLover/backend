package team.klover.server.domain.community.commPost.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DetailCommPostDto {
    private Long memberId;
    private Long id;
    private String nickname;
    private String profileImageUrl;
    private int likeCount;
    private long commentCount;
    private Double mapX;
    private Double mapY;
    private String content;
    private List<String> imageUrls;
    private LocalDateTime createDate;
}
