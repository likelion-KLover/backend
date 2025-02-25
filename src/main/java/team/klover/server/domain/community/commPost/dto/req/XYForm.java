package team.klover.server.domain.community.commPost.dto.req;

import lombok.Data;

@Data
public class XYForm {
    private Double mapX;
    private Double mapY;
    private Integer radius; // 반경 값 (미터 단위)
}
