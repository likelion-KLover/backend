package team.klover.server.domain.community.commPost.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import team.klover.server.domain.tour.tourPost.dto.res.TourPostDto;

@Getter
@AllArgsConstructor
public class CombinedPostResponse {
    private Page<CommPostDto> commPosts;
    private Page<TourPostDto> tourPosts;
}
