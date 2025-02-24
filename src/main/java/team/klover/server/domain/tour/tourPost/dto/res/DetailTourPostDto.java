package team.klover.server.domain.tour.tourPost.dto.res;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailTourPostDto {
    private String contentId;
    private String title;
    private String addr1;
    private String firstImage;
    private String homepage;
    private String mapX;
    private String mapY;
    private String overview;
}
