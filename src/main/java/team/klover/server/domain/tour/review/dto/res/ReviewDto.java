package team.klover.server.domain.tour.review.dto.res;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewDto {
    private Long testMemberId;
    private String testMemberNickname;
    private String content;
    private int rating;
    private LocalDateTime createDate;
}
