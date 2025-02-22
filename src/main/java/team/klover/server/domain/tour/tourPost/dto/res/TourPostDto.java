package team.klover.server.domain.tour.tourPost.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TourPostDto {
    private String contentId;
    private String title;
    private String addr1;
    private String firstImage;
    private String map_x;
    private String map_y;
}
