package team.klover.server.domain.community.commPost.dto.res;

import lombok.*;
import team.klover.server.global.elasticsearch.commpost.doc.CommPostDoc;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommPostDto {
    private Long memberId;
    private String nickname;
    private Double mapX;
    private Double mapY;
    private List<String> imageUrls;
    private LocalDateTime createDate;

    public CommPostDto(CommPostDoc commPostDoc){
        memberId = commPostDoc.getMember_id();
        nickname = commPostDoc.getNickname();
        mapX = commPostDoc.getLocation().getLon();
        mapY = commPostDoc.getLocation().getLat();
        imageUrls = commPostDoc.getImage_url();
        createDate = commPostDoc.getCreate_date().toLocalDateTime();
    }
}
