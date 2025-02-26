package team.klover.server.domain.community.commPost.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommPostForm {
    private Double mapX;
    private Double mapY;
    private String content;
    private String imageUrl;
}
