package team.klover.server.domain.tour.tourPost.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TourPostDto {
    private Long contentId;
    private Long commonPlaceId;
    private Double avgRating;
    private String title;
    private String addr1;
    private String firstImage;
    private Double mapX;
    private Double mapY;
}
