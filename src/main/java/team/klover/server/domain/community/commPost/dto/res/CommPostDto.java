package team.klover.server.domain.community.commPost.dto.res;

import lombok.*;
import team.klover.server.global.elasticsearch.commpost.doc.CommPostDoc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommPostDto {
    private Long id;
    private Long memberId;
    private String nickname;
    private Double mapX;
    private Double mapY;
    private List<String> imageUrls;
    private LocalDateTime createDate;

    public CommPostDto(CommPostDoc commPostDoc){
        id = commPostDoc.getId();
        memberId = commPostDoc.getMember_id();
        nickname = commPostDoc.getNickname();
        mapX = commPostDoc.getLocation().getLon();
        mapY = commPostDoc.getLocation().getLat();
        createDate = commPostDoc.getCreate_date().toLocalDateTime();

        if(commPostDoc.getImage_urls() != null){
            String response = commPostDoc.getImage_urls();
            response = response.replace("[","").replace("]","").replace("\"","").strip();
            if(response.isBlank()) return;
            String[] urls = response.split(",");
            imageUrls = new ArrayList<>();
            for(String url : urls){
                String plainUrl = url.strip();
                imageUrls.add(plainUrl);
            }
        }
    }
}
